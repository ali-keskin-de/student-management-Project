package com.project.contactmessage.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity // Bu class'in hibernate bir Entity class'i oldugu bildirilmis olur.
@Getter // Lombok'tan gelir boilerpalte code'larin önüne gecer. @Data da burda kullanilir Getter Setter yerine ancak Data ile birlikte toString gibi yapilarda geliyor.
@Setter // Peki nerde @Data kullanmaliyiz? Getter ve Setter bizim ihtiyacimizi görüyorsa bu ikisi kullanilir. Cünkü data ile equal, toString, hashCode gibi yapilarda gelir.
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) //  bir nesne create edilirken ilgili feild'larin setlenmesini istiyorsak;
// mesela bir nesne create ettik ve ikici bir nesne daha create etmek istiyoruz ve bu ikinci nesnenin ilk 9 field'i ayni olacak
// ve sadece 10. degistirilmesi gerekiyorsa  bu+lari tekrar setlemek yerine bunlari ilk nesneden kolonlamak mümkündür.
// Bununda arkaplanda calisacak Builder desing patter'lar ile yapabiliriz. Bunun icin (toBuilder = true) yazmak gerek parameter olarak.
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Email(message = "please enter valid email")
    private String email;

    @NotNull
    private String subject;

    @NotNull
    private String message;

    // @JsonFormat  neden ihtiyac duyariz? DB gelen tarih bilgisini bize istedigimiz  tarzda ve formatta görmek istiyorsak kullaniriz. Ancak database detayli kayit yapar.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH:mm", timezone = "US")
    private LocalDateTime dateTime;


}
