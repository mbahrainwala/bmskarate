package ca.bmskarate.controller;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.security.SessionTokenManager;
import ca.bmskarate.vo.UserVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/api")
public class FileDownloadController {
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> download(@RequestParam String token, final HttpServletResponse response) throws BmsException {
        Authentication auth = SessionTokenManager.getToken(token);
        if(auth==null || auth.getPrincipal()==null)
            throw new BmsException("unauthorised");

        UserVo user =  (UserVo)auth.getPrincipal();

        response.setContentType("video/mp4");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=sample.mp4");

        final File file = new File("D:"
            + File.separator + "SampleVideo_1280x720_1mb.mp4");
        if (file.exists() && file.canRead()) {
            try {
                final FileInputStream inputStream = new FileInputStream(file);
                StreamingResponseBody stream = out -> {
                    final FilterOutputStream fos = new FilterOutputStream(response.getOutputStream());
                    byte[] bytes=new byte[1024];
                    int length;
                    while ((length=inputStream.read(bytes)) >= 0) {
                        fos.write(bytes, 0, length);
                    }
                    inputStream.close();
                    fos.close();
                };
                return new ResponseEntity(stream, HttpStatus.OK);
            }catch(Exception e){
                throw new BmsException("Error downloading file.");
            }
        }else
            throw new BmsException("You do not have access to read the file.");
    }
}
