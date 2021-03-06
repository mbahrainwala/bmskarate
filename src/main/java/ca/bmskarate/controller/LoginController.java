package ca.bmskarate.controller;

import ca.bmskarate.controller.request.LoginRequest;
import ca.bmskarate.controller.response.LoginResponse;
import ca.bmskarate.exception.BmsException;
import ca.bmskarate.security.SessionTokenManager;
import ca.bmskarate.service.UserService;
import ca.bmskarate.util.APIErrors;
import ca.bmskarate.vo.StudentVo;
import ca.bmskarate.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginReq, HttpServletRequest httpServletRequest) throws BmsException, CloneNotSupportedException, MessagingException {
        LoginResponse resp = new LoginResponse();

        UserVo userVo = userService.getUserByEmailPassword(loginReq.getEmailId(), loginReq.getPassword());

        if(userVo!=null) {
            userVo.setLastLoggedIn(new Date());
            userService.saveUser(userVo);
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(userVo.getType()));
            Authentication authentication = new UsernamePasswordAuthenticationToken(userVo, null, authorities);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);

            UserVo retUser = userVo.clone();
            //clear sensitive data
            retUser.setPassword("");
            retUser.setSecretAns("");

            //go through students to remove cyclic refrence
            for(StudentVo student:retUser.getStudents()){
                student.setParent(null);
            }

            resp.setUser(retUser);
            resp.setToken(SessionTokenManager.setToken(authentication, httpServletRequest.getRemoteAddr()));
            return ResponseEntity.ok(resp);
        }

        throw new BmsException("Invalid emailId/password");
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.POST)
    public ResponseEntity<String> logout(Principal auth, @RequestHeader("X-SESSION-ID") String token) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(null);

        SessionTokenManager.removeToken(token);
        return ResponseEntity.ok("logout success");
    }
}

