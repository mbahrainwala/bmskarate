package ca.bmskarate.service;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.repositories.StudentRepository;
import ca.bmskarate.repositories.UserRepository;
import ca.bmskarate.util.SecurityUtils;
import ca.bmskarate.util.YesNo;
import ca.bmskarate.vo.StudentVo;
import ca.bmskarate.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    MailSenderService mailService;

    @Autowired
    Environment env;

    public enum AllowedUserTypes{U ("User"), A("Admin"), S("Super User");
        private final String desc;

        AllowedUserTypes(String desc) {
            this.desc = desc;
        }

        public String getDescription() {
            return this.desc;
        }
    };
    public enum SecretQuestions{A ("Mothers maiden name"),B ("Model of your first car"),C("Name of your high school");
        private final String desc;

        SecretQuestions(String desc) {
            this.desc=desc;
        }

        public String getDescription() {
            return this.desc;
        }
    };

    @Transactional
    public Optional<UserVo> getUserById(long id){return this.userRepository.findById(id);}

    @Transactional
    public UserVo getUserByEmail(String emailId){
        List<UserVo> userList = userRepository.findByEmailIdIgnoreCase(emailId);
        if(userList!=null && userList.size()==1)
            return userList.get(0);
        else
            return null;
    }

    @Transactional
    public UserVo getUserByEmailPassword(String emailId, String password){
        password = SecurityUtils.getMD5Hash(password);
        List<UserVo> userList = userRepository.findByEmailIdAndPasswordIgnoreCase(emailId, password);
        if(userList!=null && userList.size()==1)
            return userList.get(0);
        else
            return null;
    }

    @Transactional
    public List<UserVo> getUserByLastNameLike(String lastName){
        return userRepository.findByLastNameStartsWithIgnoreCase(lastName, Sort.by("lastName").and(Sort.by("firstName")));
    }

    @Transactional
    public void saveUser(UserVo vo) throws BmsException, MessagingException {
        //validate user before saving
        String errors = validateUser(vo);
        if(errors!=null && errors.length()>0) {
            throw new BmsException(errors);
        }

        if(vo.getId()<1) {
            UserVo userVo = getUserByEmail(vo.getEmailId());
            if(userVo!=null && userVo.getId()>0){
                throw new BmsException("Email already exists");
            }

            String newPass = genPassword(6);
            String emailText = env.getProperty("register.mail.text");
            emailText = emailText.replaceAll("'fname'", vo.getFirstName());
            emailText = emailText.replaceAll("'lname'", vo.getLastName());
            emailText = emailText.replaceAll("'password'", newPass);
            mailService.sendMail(vo.getEmailId(), env.getProperty("register.mail.subject"), emailText);
            vo.setPasswordAsEncrypt(newPass);//TODO: password needs to be done via email

            vo.setPremium(YesNo.N.toString());
            vo.setType(AllowedUserTypes.U.toString());
            vo.setCreatedDate(new Date());
        }else{
            //user already exists
            Optional<UserVo> existingOptUser = getUserById(vo.getId());
            if(existingOptUser==null || !existingOptUser.isPresent())
                throw new BmsException("User does not exist");

            UserVo user = existingOptUser.get();
            vo.setEmailId(user.getEmailId()); //email address cannot be changed

            if(vo.getStudents()==null)
                vo.setStudents(user.getStudents());//students cannot be modified here.
        }
        this.userRepository.save(vo);
    }

    @Transactional
    public void forgotPassword(UserVo vo) throws BmsException, MessagingException {
        UserVo userVo = getUserByEmail(vo.getEmailId());

        if(userVo==null || !userVo.getSecretAns().equals(vo.getSecretAns())){
            throw new BmsException("User not found/wrong secret answer");
        }

        String newPass = genPassword(6);
        String emailText = env.getProperty("forgot.mail.text");
        emailText = emailText.replaceAll("'fname'", userVo.getFirstName());
        emailText = emailText.replaceAll("'lname'", userVo.getLastName());
        emailText = emailText.replaceAll("'password'", newPass);
        mailService.sendMail(vo.getEmailId(), env.getProperty("forgot.mail.subject"), emailText);
        userVo.setPasswordAsEncrypt(newPass);

        saveUser(userVo);
    }

    @Transactional
    public void addStudentToUser(long userId, long studentId) throws BmsException, MessagingException {
        Optional<UserVo> userOpt = getUserById(userId);
        if(userOpt==null || !userOpt.isPresent())
            throw new BmsException("User does not exist");

        UserVo user= userOpt.get();
        for(StudentVo student:user.getStudents()){
            if(student.getId() == studentId)
                throw new BmsException("Student already exists for user");
        }

        Optional<StudentVo> studentOpt = studentRepository.findById(studentId);
        if(studentOpt==null || !studentOpt.isPresent())
            throw new BmsException("Student does not exist");

        StudentVo student = studentOpt.get();
        student.setParent(user);
        studentRepository.save(student);

        user.getStudents().add(studentOpt.get());
        saveUser(user);
    }

    @Transactional
    public void removeStudentFromUser(long userId, long studentId) throws BmsException, MessagingException {
        Optional<UserVo> userOpt = getUserById(userId);
        if(userOpt==null || !userOpt.isPresent())
            throw new BmsException("User does not exist");

        UserVo user= userOpt.get();
        boolean userExists = false;
        for(StudentVo student:user.getStudents()){
            if(student.getId() == studentId) {
                userExists = true;
                user.getStudents().remove(student);
                break;
            }
        }

        if(userExists) {
            Optional<StudentVo> studentOpt = studentRepository.findById(studentId);
            if(studentOpt!=null && studentOpt.isPresent()) {
                StudentVo student = studentOpt.get();
                student.setParent(null);
                studentRepository.save(student);
            }
        }

        if(!userExists)
            throw new BmsException("Student does not exist");

        saveUser(user);
    }

    private String validateUser(UserVo vo){
        boolean hasError = false;
        String error="";
        if(vo.getEmailId() == null || vo.getEmailId().isEmpty() || !vo.getEmailId().contains("@") || !vo.getEmailId().contains(".")) {
            error += "Invalid EmailId";
            hasError=true;
        }

        if(vo.getFirstName()==null || vo.getFirstName().isEmpty()) {
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid First Name";
        }

        if(vo.getLastName()==null || vo.getLastName().isEmpty()) {
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid Last Name";
        }

        if(vo.getPhone() != null)
        {
            if(!vo.getPhone().matches("^[2-9]\\d{2}-\\d{3}-\\d{4}$")){
                if(hasError)
                    error += ", ";
                hasError=true;
                error += "Phone number must be of type xxx-xxx-xxxx";
            }
        }

        if(vo.getPostalCode() == null || vo.getPostalCode().isEmpty()){
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid Postal Code";
        }

        if(vo.getCityVo() == null){
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid City";
        }

        if(vo.getSecretAns()==null || vo.getSecretAns().isEmpty()){
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid Answer";
        }

        boolean enumExists = false;

        for(AllowedUserTypes types:AllowedUserTypes.values()){
            if(types.toString().equals(vo.getType())){
                enumExists = true;
                break;
            }
        }

        if(!enumExists){
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid User Type";
        }

        enumExists=false;
        for(SecretQuestions ques:SecretQuestions.values()){
            if(ques.toString().equals(vo.getSecretQues())){
                enumExists = true;
                break;
            }
        }

        if(!enumExists){
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid Question Selected";
        }

        return error;
    }

    private String genPassword(int n) {
        char[] pw = new char[n];
        int c = 'A';
        int r1 = 0;
        for (int i = 0; i < n; i++) {
            r1 = (int) (Math.random() * 3);
            switch (r1) {
                case 0:
                    c = '0' + (int) (Math.random() * 10);
                    break;
                case 1:
                    c = 'a' + (int) (Math.random() * 26);
                    break;
                case 2:
                    c = 'A' + (int) (Math.random() * 26);
                    break;
            }
            pw[i] = (char) c;
        }
        return new String(pw);
    }
}

