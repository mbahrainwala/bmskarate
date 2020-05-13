package ca.bmskarate.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Table(name="certificate")
@Getter
@Setter
@NoArgsConstructor
public class CertificatesVo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    long id;

    @Column(name="belt", nullable=false)
    int belt;

    @Column(name="pdflink", length = 512, nullable=false)
    String pdflink;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @Valid
    StudentVo student;
}
