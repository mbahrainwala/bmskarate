package ca.bmskarate.service;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.repositories.StudentRepository;
import ca.bmskarate.util.Belt;
import ca.bmskarate.vo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Transactional
    public void save(StudentVo vo) throws BmsException {
        String errors = validateStudent(vo);
        if(errors!=null && errors.length()>0) {
            throw new BmsException(errors);
        }

        studentRepository.save(vo);
    }

    private String validateStudent(StudentVo vo){
        boolean hasError = false;
        String error="";

        if(vo.getFirstName()==null || vo.getFirstName().isEmpty()) {
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid First Name";
        }

        if(vo.getLastName()==null || vo.getLastName().isEmpty()) {
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid Last Name";
        }

        if(vo.getStripes()<0 || vo.getStripes()>6){
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid Stripes";
        }

        boolean hasBelt = false;
        for(Belt belt:Belt.values()){
            if(belt.getId() == vo.getBelt()){
                hasBelt = true;
                break;
            }
        }

        if(!hasBelt)
        {
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid Belt";
        }

        return error;
    }
}
