package ca.bmskarate.controller;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.security.SessionTokenManager;
import ca.bmskarate.service.FileService;
import ca.bmskarate.service.UserService;
import ca.bmskarate.util.APIErrors;
import ca.bmskarate.vo.ClassVideoVo;
import ca.bmskarate.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FileDownloadController {
    @Autowired
    private FileService fileService;

    @Autowired
    private Environment env;

    @GetMapping(value="listTrainingVideo")
    public ResponseEntity<List<ClassVideoVo>> listTrainingVideo(Principal auth, @RequestParam int belt) throws BmsException {
        if(auth==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo principal = (UserVo) ((UsernamePasswordAuthenticationToken)auth).getPrincipal();

        if(UserService.AllowedUserTypes.U.toString().equals(principal.getType()) && belt>principal.getMaxBelt())
            throw new BmsException(APIErrors.NOACCESS);

        return ResponseEntity.ok(fileService.listTrainingVideos(belt));
    }

    @GetMapping(value = "/downloadTrainingVideo")
    public ResponseEntity<StreamingResponseBody> downloadTrainingVideo(@RequestParam String token,
                  final HttpServletRequest request, final HttpServletResponse response, @RequestParam long videoId) throws BmsException {
        Authentication auth = SessionTokenManager.getToken(token, request.getRemoteAddr());
        if(auth==null || auth.getPrincipal()==null)
            throw new BmsException(APIErrors.UNAUTHORISED);

        UserVo user =  (UserVo)auth.getPrincipal();
        Optional<ClassVideoVo> optFile = fileService.findTrainingVideoById(videoId);
        if(optFile==null || !optFile.isPresent())
            throw new BmsException(APIErrors.NOTFOUND);

        ClassVideoVo videoFile = optFile.get();

        if(videoFile.getBelt()>user.getMaxBelt())
            throw new BmsException(APIErrors.NOACCESS);

        response.setContentType("video/mp4");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename="+videoFile.getFileName());

        final File file = new File(env.getProperty("file.trainingVideo")
            + File.separator + videoFile.getFileName());
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
            throw new BmsException(APIErrors.NOACCESS);
    }
}
