package ca.bmskarate.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="classvideo")
@Getter
@Setter
@NoArgsConstructor
public class ClassVideoVo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    long id;

    @Column(name="filename", length=512, nullable=false)
    private String fileName;

    @Column(name="description", length=512, nullable=false)
    private String description;

    @Column(name="belt", nullable=false)
    private int belt;

    @Column(name="createddate", nullable=false)
    Date createdDate;
}
