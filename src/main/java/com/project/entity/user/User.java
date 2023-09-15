package com.project.entity.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.entity.business.LessonProgram;
import com.project.entity.business.Meet;
import com.project.entity.business.StudentInfo;
import com.project.entity.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

@Entity
@Table(name = "t_user")
public class User {

    @Id
    private Long id;

    @Column(unique = true)
    private String userName;

    @Column(unique = true)
    private String ssn;

    private String name;

    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birtDay;

    private String birthPlace;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Db'de giderken Jsonda olmasin ama Db'e gelirken json'da  olsun ve Db'e  yazilsin
    private String password;

    @Column(unique =true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;


// built_in true yapatigimizda bu kullanici silinemez ve silinmesi bile teklif edilemez. Bunu delete methodlarinda kontrol etmeliyiz.
    private Boolean built_in;

    private String motherName;

    private String fatherName;


    private int studentNumber;

    private boolean isActive;

    private Boolean isAdvisor;

    private Long advisorTeacherId;// Bu field student lar icin eklendi.

    private Gender gender;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)// Db'de giderken Jsonda olmasin ama Db'e gelirken json'da  olsun ve Db'e  yazilsin
    @OneToOne
    private UserRole userRole;



    @OneToMany(mappedBy = "teacher",cascade = CascadeType.REMOVE)//cascade = CascadeType.REMOVE bunun anlami bir ögrenci silinirse bu ögr. infolarida silinsin diye.
    private List<StudentInfo> studentInfos; // set olabilir ??

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_lessonprogram",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_program_id")
    )
    private Set<LessonProgram> lessonsProgramList;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "meet_student_table",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "meet_id")
    )
    private List<Meet> meetList;
}
