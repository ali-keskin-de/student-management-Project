package com.project.contactmessage.service;


import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.dto.ContactMessageUpdateRequest;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.exception.ResourceNotFoundException;
import com.project.contactmessage.mapper.ContactMessageMapper;
import com.project.contactmessage.message.Messages;
import com.project.contactmessage.repository.ContactMessageRepository;
import com.project.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private ContactMessageMapper contactMessageMapper;

    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {

      ContactMessage contactMessage = contactMessageMapper.requestToContactMessage(contactMessageRequest);
      ContactMessage savedData = contactMessageRepository.save(contactMessage);

      return ResponseMessage.<ContactMessageResponse>builder()
              .message("Contact Message Created Successfully")
              .httpStatus(HttpStatus.CREATED)
              .object(contactMessageMapper.contactMessageToResponse(savedData))
              .build();




    }

    public Page<ContactMessageResponse> getAll(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")){
             pageable = PageRequest.of(page,size, Sort.by(sort).descending());
        }

        return contactMessageRepository.findAll(pageable).map(contactMessageMapper::contactMessageToResponse);
    }

    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")){
            pageable = PageRequest.of(page,size, Sort.by(sort).descending());
        }

        return contactMessageRepository.findByEmailEquals(email,pageable)
                .map(contactMessageMapper::contactMessageToResponse);
    }

    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String sort, String type) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")){
            pageable = PageRequest.of(page,size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findBySubjectEquals(subject,pageable)
                .map(contactMessageMapper::contactMessageToResponse);

    }


    public ContactMessage getContactMessageById(Long contactMessageId) {

      return  contactMessageRepository.findById(contactMessageId).orElseThrow(
                ()-> new ResourceNotFoundException(Messages.NOT_FOUND_MESSAGE));
    }

    // Not: deleteByIdParam *************************************
    public String deleteById(Long contactMessageId) {

        // contactMessageRepository.delete(getContactMessageById(contactMessageId));
        getContactMessageById(contactMessageId);
        contactMessageRepository.deleteById(contactMessageId);
        return Messages.CONTACT_MESSAGE_DELETE;
    }


    // Not: updateById ******************************************
    public ResponseMessage<ContactMessageResponse> updateById(Long id, ContactMessageUpdateRequest contactMessageUpdateRequest) {
        ContactMessage contactMessage = getContactMessageById(id);

        if(contactMessageUpdateRequest.getMessage() !=null){
            contactMessage.setMessage(contactMessageUpdateRequest.getMessage());
        }
        if(contactMessageUpdateRequest.getSubject() != null) {
            contactMessage.setSubject(contactMessageUpdateRequest.getSubject());
        }
        if(contactMessageUpdateRequest.getName() != null){
            contactMessage.setName(contactMessageUpdateRequest.getName());
        }
        if(contactMessageUpdateRequest.getEmail() != null) {
            contactMessage.setEmail(contactMessageUpdateRequest.getEmail());
        }

        contactMessage.setDateTime(LocalDateTime.now());
        contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Created Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(contactMessageMapper.contactMessageToResponse(contactMessage))
                .build();
    }
}