package ca.bmskarate.controller;

import ca.bmskarate.controller.request.UserRequest;
import ca.bmskarate.exception.BmsException;
import ca.bmskarate.service.CityService;
import ca.bmskarate.service.UserService;
import ca.bmskarate.util.APIErrors;
import ca.bmskarate.util.SecurityUtils;
import ca.bmskarate.util.YesNo;
import ca.bmskarate.vo.CityVo;
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
                throw new BmsException("User is not allowed to change user type.");
            }

            if(!UserService.AllowedUserTypes.A.toString().equals(principal.getType())
                    && !userReq.getSesnei().equals(origUser.getSesnei())){
                throw new BmsException("User is not allowed to change sensei status.");
            }

            if(userReq.getPremium()==null)
                userReq.setPremium(origUser.getPremium());

            if(!UserService.AllowedUserTypes.A.toString().equals(principal.getType())
                    && !userReq.getPremium().equals(origUser.getPremium())){
                throw new BmsException("User is not allowed to change membership.");
            }

            //handle sensitive data encryption
            UserVo newUser = getUser(userReq);
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
            return ResponseEntity.ok("Invalid userId");
    }

    @RequestMapping(value = "/api/findUser", method = RequestMethod.GET)
    public ResponseEntity<List<UserVo>> findUser(Principal auth, @RequestParam @NotNull String lastName) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType()))
            throw new BmsException(APIErrors.NOACCESS);

        if(lastName.trim().isEmpty())
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(userService.getUserByLastNameLike(lastName.trim()));
    }

    private UserVo getUser(UserRequest req) throws BmsException {
        Optional<CityVo> cityVo = cityService.findCityById(req.getCityId());

        if(cityVo!=null && cityVo.isPresent()) {
            UserVo userVo = new UserVo();
            userVo.setId(req.getId());
            userVo.setEmailId(req.getEmailId()!=null?req.getEmailId().trim():null);
            userVo.setPasswordAsEncrypt(req.getPassword()!=null?req.getPassword().trim():null);
            userVo.setFirstName(req.getFirstName()!=null?req.getFirstName().trim():null);
            userVo.setLastName(req.getLastName()!=null?req.getLastName().trim():null);
            userVo.setAddr1(req.getAddr1()!=null?req.getAddr1().trim():null);
            userVo.setAddr2(req.getAddr2()!=null?req.getAddr2().trim():null);
            userVo.setPostalCode(req.getPostalCode()!=null?req.getPostalCode().trim():null);
            userVo.setCityVo(cityVo.get());
            userVo.setType(req.getType());
            userVo.setPhone(req.getPhone());
            userVo.setSesnei(req.getSesnei());
            userVo.setPremium(req.getPremium());

            userVo.setSecAnsAsEncrypt(req.getSecretAns()!=null?req.getSecretAns().trim():null);
            userVo.setSecretQues(req.getSecretQues()!=null?req.getSecretQues().trim():null);
            return userVo;
        }
        else {
            throw new BmsException("City Not Found");
        }
    }
}
