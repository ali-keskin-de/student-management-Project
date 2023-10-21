package com.project.contactmessage.mapper;


import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
// Injection ettigimizde bu classin springboot tarafindan okuna bilmesi icin ve class sevyesinde  oldugundan @Component koyduk. (Singleton Scope olarak)
@Component
public class ContactMessageMapper {

 // Bu method bize ContactMessageRequest alip bize Pojo class'a cevirecek.
    public ContactMessage requestToContactMessage(ContactMessageRequest contactMessageRequest){

        // Eger biz burda builder design patter kullanmasaydik önce bir obje olusturacaktik pojo class'in parametresiz constructor'indan ve burda tek tek atama yapacaktik.
        // ContactMessage.builder() bunu dedigimizde JVM bana bir ContactMessage nesnesi olustur diyoruz. setlenmesi gereken degerler setlendikten sonra en son build() method'u ile bittirmeliyiz.
        // Pojo class oldugundan bütün field'lar setlenmelidir.
        return ContactMessage.builder()
                .name(contactMessageRequest.getName())
                .subject(contactMessageRequest.getSubject())
                .message(contactMessageRequest.getMessage())
                .email(contactMessageRequest.getEmail())
                .dateTime(LocalDateTime.now())
                .build();

    }

    // Pojo'yu Reponse ceviriyor.
    // Burada gelen datalar DB olan veriler gelecek.
    public ContactMessageResponse contactMessageToResponse(ContactMessage contactMessage){
        return ContactMessageResponse.builder()
                .name(contactMessage.getName())
                .subject(contactMessage.getSubject())
                .message(contactMessage.getMessage())
                .email(contactMessage.getEmail())
                .dateTime(contactMessage.getDateTime())
                .build();
    }
}
//return ContactMessage.builder() dedigimizde bizden nesnenin fieldlarini istiyor
