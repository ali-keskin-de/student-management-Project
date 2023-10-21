package com.project.contactmessage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



// Eger dönen status kodunuda biz setlemek istersek o zaman @ResponseStatus(HttpStatus.NOT_FOUND) kullaniriz.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
