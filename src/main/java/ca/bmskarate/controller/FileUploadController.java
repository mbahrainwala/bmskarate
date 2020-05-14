package ca.bmskarate.controller;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.util.VideoType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping(value = "/uploadTrainingVideo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadTrainingVideo(Principal principal, @RequestParam String belt
            , @RequestParam("file") MultipartFile file) throws BmsException, IOException {

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "";

        if(originalFileName.contains("..")) {
            throw new BmsException("Sorry! Filename contains invalid path sequence " + originalFileName);
        }

        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            if(!".mp4".equals(fileExtension.toLowerCase()))
                throw new BmsException("Only mp4 files are allowed");
        } catch(Exception e) {
            throw new BmsException("Only mp4 files are allowed");
        }

        Path fileStorageLocation = Paths.get("D:\\bmsFiles").toAbsolutePath().normalize();;
        fileName = VideoType.T.getDesc() +"_"+ originalFileName.toLowerCase();
        Path targetLocation = fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok("Success");
    }
}
