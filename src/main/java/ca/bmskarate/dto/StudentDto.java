package ca.bmskarate.dto;

import ca.bmskarate.vo.StudentVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentDto {
    long id;
    String number;
    String firstName;
    String lastName;
    int belt;
    int stripes;

    public StudentVo getStudentVo(){
        StudentVo vo = new StudentVo();
        vo.setId(id);
        vo.setNumber(number!=null?number.trim():null);
        vo.setFirstName(firstName!=null?firstName.trim():null);
        vo.setLastName(lastName!=null?lastName.trim():null);
        vo.setBelt(belt);
        vo.setStripes(stripes);
        return vo;
    }
}
