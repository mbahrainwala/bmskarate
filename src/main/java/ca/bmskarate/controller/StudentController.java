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

        return ResponseEntity.ok("Success");
    }

    @RequestMapping(value="/findStudents", method = RequestMethod.GET)
    public ResponseEntity<List<StudentVo>> findStudentsByLastName(Principal auth,@RequestParam @NotNull String lastName) throws BmsException{
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType())){
            throw new BmsException(APIErrors.NOACCESS);
        }

        return ResponseEntity.ok(studentService.findByLastName(lastName));
    }
}
