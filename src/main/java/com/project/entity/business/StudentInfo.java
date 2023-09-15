package com.project.entity.business;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.entity.enums.Note;
import com.project.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer absentee;

    private Double midtermExam;

    private Double finalExam;

    private Double examAverage;

    private String infoNote;

    private Note letterGrade;

    @ManyToOne // Burasi bir studentInfoda oldugumuzdan iliskinin ilk tarafi ile baslamaliyiz, bir teacher'in birden cok StudentInfo olabilir. Bu sebepten @ManyToOne
    @JsonIgnore
    private User teacher;

    @ManyToOne
    @JsonIgnore
    private User student;

    //Note:EducationTerm

    @ManyToOne
    private Lesson lesson;

    @OneToOne
    private EducationTerm educationTerm;




}
