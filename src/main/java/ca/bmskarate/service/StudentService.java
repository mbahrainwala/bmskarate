package ca.bmskarate.service;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.repositories.StudentRepository;
import ca.bmskarate.util.Belt;
import ca.bmskarate.vo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

        StudentVo student = findByNumber(vo.getNumber());
        if(student!=null && student.getId()!=vo.getId())
            throw new BmsException("Student Number Exists.");

        studentRepository.save(vo);
    }

    @Transactional
    public Optional<StudentVo> findStudentById(long id){return studentRepository.findById(id);}

    @Transactional
    public StudentVo findByNumber(String number){
        List<StudentVo> students = studentRepository.findByNumber(number);
        if(students!=null && students.size()>0)
            return students.get(0);

        return null;
    }

    @Transactional
    public List<StudentVo> findByLastName(String lastName){
        return studentRepository.findByLastNameStartsWithIgnoreCase(lastName, Sort.by("lastName").and(Sort.by("firstName")));
    }

    private String validateStudent(StudentVo vo){
        boolean hasError = false;
        String error="";

        if(vo.getNumber()==null || vo.getNumber().isEmpty()) {
            if(hasError)
                error += ", ";
            hasError=true;
            error += "Invalid Number";
        }

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
