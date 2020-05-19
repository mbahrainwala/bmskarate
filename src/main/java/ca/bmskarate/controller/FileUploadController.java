package ca.bmskarate.controller;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.service.FileService;
import ca.bmskarate.service.UserService;
import ca.bmskarate.util.APIErrors;
import ca.bmskarate.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class FileUploadController {
    @Autowired
    private FileService fileService;

    @PostMapping(value = "/uploadTrainingVideo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadTrainingVideo(Principal auth, @RequestParam int belt, @RequestParam String desc
            , @RequestParam("file") MultipartFile file) throws BmsException, IOException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType()))
            throw new BmsException(APIErrors.NOACCESS);

        fileService.saveTrainingFile(belt, desc, file);

        return ResponseEntity.ok("Update Successful");
    }
}
