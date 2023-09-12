package com.project.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Json dosyasindan istedigimiz verileri dönmesini saglar burda null olmayanlar olsun diyorum
public class ResponseMessage<E> {
    private E object;
    private HttpStatus httpStatus;
    private String message;
}