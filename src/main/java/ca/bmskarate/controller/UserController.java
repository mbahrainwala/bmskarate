package ca.bmskarate.controller;

import ca.bmskarate.controller.request.UserRequest;
import ca.bmskarate.exception.BmsException;
import ca.bmskarate.service.CityService;
import ca.bmskarate.service.UserService;
import ca.bmskarate.util.SecurityUtils;
import ca.bmskarate.util.YesNo;
import ca.bmskarate.vo.CityVo;
import ca.bmskarate.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    CityService cityService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody UserRequest userReq, HttpServletRequest httpServletRequest) throws BmsException {
        userService.saveUser(getUser(userReq));

        return ResponseEntity.ok("registration successful");
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ResponseEntity<String> forgot(@RequestBody UserRequest userReq, HttpServletRequest httpServletRequest) throws BmsException {
        userService.forgotPassword(getUser(userReq));
        return ResponseEntity.ok("reset successful");
    }

    @RequestMapping(value = "/api/updateUser", method = RequestMethod.POST)
    public ResponseEntity<String> updateUser(Principal auth, @RequestBody UserRequest userReq, HttpServletRequest httpServletRequest) throws BmsException {
        if(auth==null)
            throw new BmsException("unauthorized");

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
                throw new BmsException("User is not allowed to change user type.");
            }

            if(!UserService.AllowedUserTypes.A.toString().equals(principal.getType())
                    && !userReq.getSesnei().equals(origUser.getSesnei())){
                throw new BmsException("User is not allowed to change sensei status.");
            }

            if(!UserService.AllowedUserTypes.A.toString().equals(principal.getType())
                    && !userReq.getPremium().equals(origUser.getPremium())){
                throw new BmsException("User is not allowed to change membership.");
            }

            //handle sensitive data encryption
            UserVo newUser = getUser(userReq);
            if(userReq.getPassword()==null || userReq.getPassword().isEmpty())
                newUser.setPassword(origUser.getPassword());
            if(userReq.getSecretAns()==null || userReq.getSecretAns().isEmpty())
                newUser.setPassword(origUser.getPassword());

            if(!UserService.AllowedUserTypes.U.toString().equals(principal.getType())
                    || origUser.getPassword().equals(SecurityUtils.getMD5Hash(userReq.getOldPassword())))
                userService.saveUser(newUser);
            else
                throw new BmsException("Invalid existing password");

            return ResponseEntity.ok("Update Successful");
        }else
            return ResponseEntity.ok("Invalid userId");
    }

    //TODO:write method to get users by lastname
    private UserVo getUser(UserRequest req) throws BmsException {
        Optional<CityVo> cityVo = cityService.findCityById(req.getCityId());

        if(cityVo!=null && cityVo.isPresent()) {
            UserVo userVo = new UserVo();
            userVo.setEmailId(req.getEmailId()!=null?req.getEmailId().trim():null);
            userVo.setPasswordAsEncrypt(req.getPassword()!=null?req.getPassword().trim():null);
            userVo.setFirstName(req.getFirstName()!=null?req.getFirstName().trim():null);
            userVo.setLastName(req.getLastName()!=null?req.getLastName().trim():null);
            userVo.setAddr1(req.getAddr1()!=null?req.getAddr1().trim():null);
            userVo.setAddr2(req.getAddr2()!=null?req.getAddr2().trim():null);
            userVo.setPostalCode(req.getPostalCode()!=null?req.getPostalCode().trim():null);
            userVo.setCityVo(cityVo.get());
            userVo.setType(UserService.AllowedUserTypes.U.toString());
            userVo.setPhone(req.getPhone());
            userVo.setSesnei(YesNo.N.toString());

            userVo.setSecAnsAsEncrypt(req.getSecretAns()!=null?req.getSecretAns().trim():null);
            userVo.setSecretQues(req.getSecretQues()!=null?req.getSecretQues().trim():null);
            return userVo;
        }
        else {
            throw new BmsException("City Not Found");
        }
    }
}
