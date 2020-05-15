package ca.bmskarate.vo;

import ca.bmskarate.util.SecurityUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="user")
@Getter
@Setter
@NoArgsConstructor
public class UserVo implements Cloneable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    long id;

    @Column(name="type", length=1, nullable=false)
    String type;

    @Column(name="emailid", length=512, unique=true, nullable=false)
    String emailId;

    @Column(name="phone", length=20)
    String phone;

    @Column(name="password", length=512, nullable=false)
    String password;

    @Column(name="fname", length=512, nullable=false)
    String firstName;

    @Column(name="lsname", length=512, nullable=false)
    String lastName;

    @Column(name="addr1", length=512)
    String addr1;

    @Column(name="addr2", length=512)
    String addr2;

    @Column(name="postal", length=20, nullable=false)
    String postalCode;

    @Column(name="premium", length=1)
    String premium;

    @Column(name="secretques", length = 1,nullable=false)
    String secretQues;

    @Column(name="secretans", length = 512, nullable=false)
    String secretAns;

    @Column(name="createddate", nullable=false)
    Date createdDate;

    @Column(name="lastloggedin")
    Date lastLoggedIn;

    @ManyToOne(cascade=CascadeType.ALL)
    @NotNull
    @Valid
    CityVo cityVo;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    List<StudentVo> students;

    @Column(name="photo", length = 512)
    String photo;

    @Column(name="disabled", length = 1)
    String disabled;

    public void setPasswordAsEncrypt(String password){
        this.setPassword(SecurityUtils.getMD5Hash(password));
    }

    public void setSecAnsAsEncrypt(String secAns){
        this.setSecretAns(SecurityUtils.getMD5Hash(secAns));
    }

    public UserVo clone() throws CloneNotSupportedException {
        UserVo vo = new UserVo();
        vo.setId(id);
        vo.setType(type);
        vo.setEmailId(emailId);
        vo.setPhone(phone);
        vo.setPassword(password);
        vo.setFirstName(firstName);
        vo.setLastName(lastName);
        vo.setAddr1(addr1);
        vo.setAddr2(addr2);
        vo.setPostalCode(postalCode);
        vo.setPremium(premium);
        vo.setSecretQues(secretQues);
        vo.setSecretAns(secretAns);
        vo.setCreatedDate(createdDate);
        vo.setLastLoggedIn(lastLoggedIn);

        vo.setCityVo(cityVo.clone());

        vo.setStudents(students);

        vo.setPhoto(photo);
        vo.setDisabled(disabled);

        return vo;
    }
}
