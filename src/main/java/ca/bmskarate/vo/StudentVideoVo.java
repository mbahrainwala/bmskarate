package ca.bmskarate.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="studentvideo")
@Getter
@Setter
@NoArgsConstructor
public class StudentVideoVo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    long id;

    @Column(name="filename", length=512, nullable=false)
    private String fileName;

    @Column(name="description", length=512, nullable=false)
    private String description;

    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "student_id", nullable = false)
    private StudentVo student;

    @Column(name="createddate", nullable=false)
    Date createdDate;
}
