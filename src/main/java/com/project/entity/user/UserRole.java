package com.project.entity.user;


import com.project.entity.enums.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles") // name atributu sayesinde DB'de roles olarak tablonun ismini degistirmis olduk normalde class isminde bir tablo olusacakti.

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder   // Klonlama mantigini kullanmayacak isek (to-Builder=true) methodunu kullanmayacagiz. builder disegn patern kullaniyoruz.
public class UserRole {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   // Enum yapiyi burdaki class ile iliskilendirmek icin kullaniyoruz.
   // ve bu EnumType gelirken string olarak gelmesini istiyoruz bunun icin (EnumType.STRING)
   @Enumerated(EnumType.STRING)
   @Column(length = 20)
   private RoleType roleType;

   // EnumType bu degisken üzerinden alacagiz.
   private String roleName;

}
// Bu class; EnumType roller arasinda bir köprü görevi gören bir class'tir.
// Bu concrete class bize esneklik saglamaktadir.
// Bu esneklikten kastimiz biz rollerle ilgili bir aksiyon'a gireceksek bu concrete class üzerinden bunu gerceklestirecegiz.
