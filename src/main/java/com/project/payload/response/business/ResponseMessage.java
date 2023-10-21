package com.project.payload.response.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data // örnek olsun diye yazdik. Getter, Setter da yazabilirdik.
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Json dosyasindan istedigimiz verileri dönmesini saglar burda null olmayanlar olsun diyoruz
public class ResponseMessage<E> { // Burada <E> verilen E su anlama gelmektedir. ben bu class’a hangi nesneyi verir isem bu class o nesne türünde çalısmaya baslayacak demektir.
    private E object; // Yukarda biz bir E türünde data türünde bir veri alacaginiz söyledik ama bunun ne olacagini belirmemistik iste burda belirtmis olduk.
                     // Object sectigimizden dolayi buraya hertürlü data tipini gönderebiliriz. Bütün Entity class'larimi verebilirim.
    private HttpStatus httpStatus; // burda HttpStatus  kodunuda verebiliriz.
    private String message;  // birde message dönecegiz.

    // Eger yukardaki field'lardan birini setlemek istemez isek örnegim message setlemek istemez isek
    // ve de null olarakta gitmesini istemiyorsak class'in basina @JsonInclude(JsonInclude.Include.NON_NULL)
}
