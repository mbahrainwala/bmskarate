package ca.bmskarate.service;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.repositories.TrainingVideoRepository;
import ca.bmskarate.util.VideoType;
import ca.bmskarate.vo.ClassVideoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private Environment env;

    @Autowired
    private TrainingVideoRepository tvRepo;

    @Transactional
    public Optional<ClassVideoVo> findTrainingVideoById(long id){
        return tvRepo.findById(id);
    }

    @Transactional
    public void saveTrainingFile(int belt, String desc, MultipartFile file) throws BmsException, IOException {
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

        Path fileStorageLocation = Paths.get(env.getProperty("file.trainingVideo")).toAbsolutePath().normalize();
        Date dte = new Date();
        fileName = VideoType.T.toString() +"_"+belt+"_"+ dte.getTime()+fileExtension;
        ClassVideoVo video = new ClassVideoVo();
        video.setBelt(belt);
        video.setCreatedDate(dte);
        video.setFileName(fileName);
        video.setDescription(desc);
        tvRepo.save(video);

        Path targetLocation = fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }

    @Transactional
    public List<ClassVideoVo> listTrainingVideos(int belt){
        return tvRepo.findByBelt(belt, Sort.by("createdDate").descending());
    }
}
