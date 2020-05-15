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
public class StudentVideoVo implements Cloneable{
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

    @Column(name="comments", length=4000, nullable=false)
    private String comments;

    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "student_id", nullable = false)
    private StudentVo student;

    @Column(name="createddate", nullable=false)
    Date createdDate;
}
