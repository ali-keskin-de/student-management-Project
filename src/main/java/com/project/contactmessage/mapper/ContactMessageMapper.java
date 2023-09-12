package com.project.contactmessage.mapper;

import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component // Bu classtan bir nesne olusmasini saglar ve her gerektigine  bize bir obje olusturur ve  isi bittince imha eder.
public class ContactMessageMapper {


    public ContactMessage requestToContactMessage(ContactMessageRequest contactMessageRequest){




        return ContactMessage.builder()
                .name(contactMessageRequest.getName())
                .subject(contactMessageRequest.getSubject())
                .message(contactMessageRequest.getMessage())
                .message(contactMessageRequest.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
    }

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
