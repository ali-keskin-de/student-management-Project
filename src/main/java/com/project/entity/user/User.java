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
import java.util.ArrayList;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String ssn;

    private String name;

    private String surname;

    // @JsonFormat bu verinin alinirken kolaylik olsun diye yani su sekilde alina bilmesi icin "yyyy-MM-dd",
    // JsonFormat.Shape.STRING--> Bununla Json formatin string olarak gelebilecegini söylüyoruz.
    // patternlede istedigimiz formati belirliyoruz.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    private String birthPlace;

    // Db'den client'a giderken  Jsonda olmasin, ama client'tan  Db'e gelirken json'da  olsun ve DB'e  yazilsin-->
    // iste bunu saglayan annt. @JsonProperty(access = JsonProperty.Access.WRITE_ONLY),
    // Ama zaten biz pojo'yu client'ta göndermiyecegiz ancak gereklilik durumunda veya ess kaza gönderdik. onun icin böyle bir önlem oliyoruz.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique =true)
    private String phoneNumber;

// Bununla @Column(unique = true)  unique olmasini sagliyor.
// Ancak bu tarz datalar Requeste'lerden geldiginde  service katmaninda bularin kontrolu mutlaka yapilmali.
    @Column(unique = true)
    private String email;


// built_in true yapatigimizda bu kullanici silinemez ve silinmesi bile teklif edilemez.
// Bunu service katmaninda delete methodlarinda kontrol etmeliyiz.
    private Boolean built_in;

    private String motherName;

    private String fatherName;


    private int studentNumber;

    private boolean isActive;

    private Boolean isAdvisor;

    private Long advisorTeacherId;// Bu field student lar icin eklendi.

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Db'de giderken Jsonda olmasin ama Db'e gelirken json'da  olsun ve Db'e  yazilsin
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    private UserRole userRole;


    //cascade = CascadeType.REMOVE bunun anlami bir ögrenci silinirse bu ögr. infolarida silinsin diye.
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.REMOVE)
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

// Biz burda Teacher, Student, ... kullanicilarin tamami icin bir user entity'si olusturduk,
// ancak burda bazi field'lar bazi role type'leri icin gerekli degil bunu her rolle type icin bir entity class'i olusturarak
// cözebilecegimizi düsünebiliriz o zamanda her entity class'i icin bir controller bir service ve bir repository olacakti
// bu kadar repository DB icin yük olusturacakti ve cok fazla classtan dolayida kod okunurluluguda iyi olmayacakti ancak
// biz yukardaki yapi ile bunu cözüme kavusturmus olduk. Yani tek bir user entity'si olustursarak cok hizli bir yap saglamis olacagiz.
// Bütün field'lar bir entity'de olunca null deger görecegiz ancak suan icin en iyi yöntem bu...

