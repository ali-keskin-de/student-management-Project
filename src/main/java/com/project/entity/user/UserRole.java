package com.project.entity.user;


import com.project.entity.enums.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRole {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Enumerated(EnumType.STRING)
   @Column(length = 20)
   private RoleType roleType;

   private String roleName;

}
