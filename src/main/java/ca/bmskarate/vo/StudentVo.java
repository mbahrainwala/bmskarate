package ca.bmskarate.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;

@Entity
@Table(name="student")
@Getter
@Setter
@NoArgsConstructor
public class StudentVo implements Cloneable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    long id;

    @Column(name="number", length=512, nullable=false, unique = true)
    String number;

    @Column(name="fname", length=512, nullable=false)
    String firstName;

    @Column(name="lname", length=512, nullable=false)
    String lastName;

    @Column(name="belt", nullable=false)
    int belt;

    @Column(name="stripes", nullable=false)
    int stripes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @Valid
    UserVo parent;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    List<StudentVideoVo> video;

    @Column(name="photo", length = 512)
    String photo;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    List<CertificatesVo> certificates;

    public StudentVo clone() throws CloneNotSupportedException {
        super.clone();
        StudentVo vo = new StudentVo();
        vo.setId(id);
        vo.setNumber(number);
        vo.setFirstName(firstName);
        vo.setLastName(lastName);
        vo.setBelt(belt);
        vo.setStripes(stripes);
        vo.setParent(parent);
        vo.setVideo(video);
        vo.setPhoto(photo);
        vo.setCertificates(certificates);

        return vo;
    }
}
