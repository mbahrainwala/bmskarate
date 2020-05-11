package ca.bmskarate.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="city")
@Getter
@Setter
@NoArgsConstructor
public class CityVo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    long id;


    @Column(name="cityname", length=512, nullable=false)
    private String cityName;

    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "province_id")
    private ProvinceVo province;
}
