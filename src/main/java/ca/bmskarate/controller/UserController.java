package ca.bmskarate.controller;

import ca.bmskarate.dto.UserDto;
import ca.bmskarate.exception.BmsException;
import ca.bmskarate.service.CityService;
import ca.bmskarate.service.UserService;
import ca.bmskarate.util.APIErrors;
import ca.bmskarate.util.SecurityUtils;
import ca.bmskarate.vo.CityVo;
import ca.bmskarate.vo.StudentVo;
import ca.bmskarate.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    CityService cityService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody UserDto userReq, HttpServletRequest httpServletRequest) throws BmsException {

        Optional<CityVo> cityVo = cityService.findCityById(userReq.getCityId());
        if(cityVo!=null && cityVo.isPresent()) {
            userService.saveUser(userReq.getUserVo(cityVo.get()));
        }else {
            throw new BmsException("City Not Found");
        }

        return ResponseEntity.ok("registration successful");
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ResponseEntity<String> forgot(@RequestBody UserDto userReq, HttpServletRequest httpServletRequest) throws BmsException {

        Optional<CityVo> cityVo = cityService.findCityById(userReq.getCityId());
        if(cityVo!=null && cityVo.isPresent()) {
            userService.forgotPassword(userReq.getUserVo(cityVo.get()));
        }else {
            throw new BmsException("City Not Found");
        }

        return ResponseEntity.ok("reset successful");
    }

    @RequestMapping(value = "/api/updateUser", method = RequestMethod.POST)
    public ResponseEntity<String> updateUser(Principal auth, @RequestBody UserDto userReq, HttpServletRequest httpServletRequest) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(userReq.getId()>0) {
            Optional<UserVo> optionalUser = userService.getUserById(userReq.getId());
            if(optionalUser == null || !optionalUser.isPresent())
                throw new BmsException("User not found");

            UserVo origUser = optionalUser.get();

            if(userReq.getType()==null || userReq.getType().isEmpty())
                userReq.setType(origUser.getType());

            if(!UserService.AllowedUserTypes.S.toString().equals(principal.getType())
                    && !origUser.getType().equals(userReq.getType())){
                userReq.setType(origUser.getType());
            }

            if(userReq.getPremium()==null)
                userReq.setPremium(origUser.getPremium());

            if(!UserService.AllowedUserTypes.A.toString().equals(principal.getType())
                    && !userReq.getPremium().equals(origUser.getPremium())){
                throw new BmsException("User is not allowed to change membership.");
            }

            UserVo newUser;
            Optional<CityVo> cityVo = cityService.findCityById(userReq.getCityId());
            if(cityVo!=null && cityVo.isPresent()) {
                newUser = userReq.getUserVo(cityVo.get());
            }else {
                throw new BmsException("City Not Found");
            }

            //handle sensitive data encryption
            if(userReq.getPassword()==null || userReq.getPassword().trim().isEmpty())
                newUser.setPassword(origUser.getPassword());
            else{
                if(!userReq.getPassword().equals(userReq.getConfirmPassword()))
                    throw new BmsException("New password and confirm passwords do not match");
            }

            if(userReq.getSecretAns()==null || userReq.getSecretAns().isEmpty())
                newUser.setSecretAns(origUser.getSecretAns());

            newUser.setCreatedDate(origUser.getCreatedDate());
            newUser.setLastLoggedIn(origUser.getLastLoggedIn());

            if(!UserService.AllowedUserTypes.U.toString().equals(principal.getType())
                    || origUser.getPassword().equals(SecurityUtils.getMD5Hash(userReq.getOldPassword())))
                userService.saveUser(newUser);
            else
                throw new BmsException("Invalid existing password");

            return ResponseEntity.ok("Update Successful");
        }else
            throw new BmsException("Invalid userId");
    }

    @RequestMapping(value = "/api/getUser", method = RequestMethod.GET)
    public ResponseEntity<UserVo> getUser(Principal auth, @RequestParam @NotNull long id) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType()))
            throw new BmsException(APIErrors.NOACCESS);

        Optional<UserVo> userOpt =  userService.getUserById(id);
        if(userOpt!=null && userOpt.isPresent()){
            UserVo user = userOpt.get();

            for(StudentVo student:user.getStudents()){
                student.setParent(null);
            }

            return ResponseEntity.ok(user);
        }

        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/api/findUser", method = RequestMethod.GET)
    public ResponseEntity<List<UserVo>> findUser(Principal auth, @RequestParam @NotNull String lastName) throws BmsException, CloneNotSupportedException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType()))
            throw new BmsException(APIErrors.NOACCESS);

        if(lastName.trim().isEmpty())
            return ResponseEntity.ok(new ArrayList<>());

        List<UserVo> userList = userService.getUserByLastNameLike(lastName.trim());
        List<UserVo> retUserList = new ArrayList<>(userList.size());
        for(UserVo user:userList){
            UserVo retVo = user.clone();
            retVo.setSecretAns("");
            retVo.setPassword("");
            for(StudentVo student:retVo.getStudents()){
                student.setParent(null);
            }
            retUserList.add(retVo);
        }

        return ResponseEntity.ok(retUserList);
    }

    @RequestMapping(value = "/api/addStudentToUser", method = RequestMethod.PATCH)
    public ResponseEntity<String> addStudentToUser(Principal auth, @RequestParam @NotNull long userId, @RequestParam @NotNull long studentId) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType()))
            throw new BmsException(APIErrors.NOACCESS);

        userService.addStudentToUser(userId, studentId);

        return ResponseEntity.ok(APIErrors.SUCCESS);
    }

    @RequestMapping(value = "/api/removeStudentFromUser", method = RequestMethod.PATCH)
    public ResponseEntity<String> removeStudentFromUser(Principal auth, @RequestParam @NotNull long userId, @RequestParam @NotNull long studentId) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType()))
            throw new BmsException(APIErrors.NOACCESS);

        userService.removeStudentFromUser(userId, studentId);

        return ResponseEntity.ok(APIErrors.SUCCESS);
    }
}
