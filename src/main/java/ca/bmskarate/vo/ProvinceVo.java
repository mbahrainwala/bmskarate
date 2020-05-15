package ca.bmskarate.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="province")
@Getter
@Setter
@NoArgsConstructor
public class ProvinceVo implements Cloneable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    long id;

    @Column(name="provinceName", length=512, nullable=false, unique=true)
    private String provinceName;
}
