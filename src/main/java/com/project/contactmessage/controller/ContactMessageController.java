package com.project.contactmessage.controller;


import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.service.ContactMessageService;
import com.project.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;


    //Note: save()*********************************************************
    @PostMapping("/save") // http://localhost:8080/contactMessages/save
    public ResponseMessage<ContactMessageResponse> save(@RequestBody @Valid ContactMessageRequest contactMessageRequest){

        return contactMessageService.save(contactMessageRequest);

    }

    // Not: getAll() ******************************************************************************
    @GetMapping("/getAll")// http://localhost:8080/contactMessages/page=1&size=1&sort=dateTime&direction=ASC
    public Page<ContactMessageResponse> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size,
                                               @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
                                               @RequestParam(value = "type", defaultValue = "desc") String type
                                               ){
        return contactMessageService.getAll(page,size,sort,type);



    }
}
