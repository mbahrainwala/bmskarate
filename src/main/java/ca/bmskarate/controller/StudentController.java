package ca.bmskarate.controller;

import ca.bmskarate.dto.StudentDto;
import ca.bmskarate.exception.BmsException;
import ca.bmskarate.service.StudentService;
import ca.bmskarate.service.UserService;
import ca.bmskarate.util.APIErrors;
import ca.bmskarate.vo.StudentVo;
import ca.bmskarate.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class StudentController {
    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/saveStudent", method = RequestMethod.POST)
    public ResponseEntity<String> addStudent(Principal auth, @RequestBody StudentDto req) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType())){
            throw new BmsException(APIErrors.NOACCESS);
        }

        studentService.save(req.getStudentVo());

        return ResponseEntity.ok("Update Successful");
    }

    @RequestMapping(value="/findStudents", method = RequestMethod.GET)
    public ResponseEntity<List<StudentVo>> findStudentsByLastName(Principal auth,@RequestParam @NotNull String lastName) throws BmsException{
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType())){
            throw new BmsException(APIErrors.NOACCESS);
        }

        List<StudentVo> studentList = studentService.findByLastName(lastName);
        for(StudentVo student:studentList){
            if(student.getParent()!=null)
                student.getParent().setStudents(null); // remove cyclic parent list
        }

        return ResponseEntity.ok(studentList);
    }

    @RequestMapping(value="/getStudents", method = RequestMethod.GET)
    public ResponseEntity<StudentVo> getStudent(Principal auth,@RequestParam @NotNull long id) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType())){
            throw new BmsException(APIErrors.NOACCESS);
        }

        Optional<StudentVo> ret = studentService.findStudentById(id);
        if(ret!=null && ret.isPresent()) {
            StudentVo student = ret.get();
            if(student.getParent()!=null)
                student.getParent().setStudents(null); // remove cyclic parent list
            return ResponseEntity.ok(student);
        }

        return ResponseEntity.ok(null);
    }
}
