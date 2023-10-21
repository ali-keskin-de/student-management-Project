package com.project.contactmessage.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContactMessageResponse {

    private String name;
    private String email;
    private String subject;
    private String message;
// Database'de olan tarih bilgisi oldukca karmasik olabilir. Bunun client'a dönerken daha okuru bir data gitmesini istiyorsak;
// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm") bunu yapariz.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;


}
// Client'a gidecek Classlarimiza Response diyoruz.
// buraya bir validation yapmayiz cünkü bu data Database gitmeyecek ve zaten DB geliyor. bu class sadece cliente dönmek icin kullanilir.
