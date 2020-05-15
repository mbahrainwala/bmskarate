package ca.bmskarate.dto;

import ca.bmskarate.vo.CityVo;
import ca.bmskarate.vo.UserVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    long id;
    String emailId;
    String password;
    String phone;
    String firstName;
    String lastName;
    String addr1;
    String addr2;
    String postalCode;
    String premium;
    String secretQues;
    String secretAns;
    int cityId;

    String type;

    String oldPassword;
    String confirmPassword;
    
    public UserVo getUserVo(CityVo city){
        UserVo userVo = new UserVo();
        userVo.setId(this.getId());
        userVo.setEmailId(this.getEmailId()!=null?this.getEmailId().trim():null);
        userVo.setPasswordAsEncrypt(this.getPassword()!=null?this.getPassword().trim():null);
        userVo.setFirstName(this.getFirstName()!=null?this.getFirstName().trim():null);
        userVo.setLastName(this.getLastName()!=null?this.getLastName().trim():null);
        userVo.setAddr1(this.getAddr1()!=null?this.getAddr1().trim():null);
        userVo.setAddr2(this.getAddr2()!=null?this.getAddr2().trim():null);
        userVo.setPostalCode(this.getPostalCode()!=null?this.getPostalCode().trim():null);
        userVo.setType(this.getType());
        userVo.setPhone(this.getPhone());
        userVo.setPremium(this.getPremium());
        userVo.setCityVo(city);

        userVo.setSecAnsAsEncrypt(this.getSecretAns()!=null?this.getSecretAns().trim():null);
        userVo.setSecretQues(this.getSecretQues()!=null?this.getSecretQues().trim():null);
        return userVo;
    }
}
