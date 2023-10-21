package com.project.contactmessage.controller;


import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.dto.ContactMessageUpdateRequest;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.service.ContactMessageService;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

 // burada bizim yapimiz RestFull mimariye uygun olacagindan @Controller ile degilde,
 // bunun Restfull oldugunu belirten @RestController ile annote ediyoruz.
@RestController
@RequestMapping("/contactMessages") //Gellen Requestleri mapp'lemek icin bunun ile annote ediyoruz.
// Burada parantez icine yazdigim bir endpoint("/contactMessages")  bu endpointle gelen requestler burda mapp'lenecek.
@RequiredArgsConstructor
public class ContactMessageController {
// Burdada controller'dan sonraki layer olan service katmani injekte edilmistir.
    private final ContactMessageService contactMessageService;

    /**
     * {
     *     "name": "Mirac",
     *     "email": "aaa@bbb.com",
     *     "subject": "deneme",
     *     "message": "this is my message"
     * }
     */ // Ornek JSON

    // Not: save() **************************************************
    // Bize gelen Json dosyasini almamiza yarayan @RequestBody dir ,
    // @Valid Request classindan gelecek olan dosyayi validationdan gecir demektir.
    @PostMapping("/save") // http://localhost:8080/contactMessages/save   + POST + JSON
    public ResponseMessage<ContactMessageResponse> save(@RequestBody @Valid ContactMessageRequest contactMessageRequest){

        return contactMessageService.save(contactMessageRequest);
    }

    // Not: getAll() *************************************************
    @GetMapping("/getAll") // http://localhost:8080/contactMessages/getAll
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<ContactMessageResponse> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return contactMessageService.getAll(page,size,sort,type);
    }

    // Not: searchByEmail ***************************************

     // Burada email'i requestparam ile alacagimizdan http://localhost:8080/contactMessages/searchByEmail sonra asagidaki gibi emaili eklemeliyiz
     // digerlerini endpoint'a yazmak zorunda degiliz cünkü onlarin default degerleri var ama emaili yazmak zorundayiz default degeri yok.
    @GetMapping("/searchByEmail") // http://localhost:8080/contactMessages/searchByEmail?email=aaa@bbb.com
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<ContactMessageResponse> searchByEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return contactMessageService.searchByEmail(email,page,size,sort,type);
    }

    // Not: searchBySubject *************************************
    @GetMapping("/searchBySubject") // http://localhost:8080/contactMessages/searchBySubject?subject=deneme
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<ContactMessageResponse> searchBySubject(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return contactMessageService.searchBySubject(subject,page,size,sort,type);
    }

    // Not: deleteByIdParam *************************************
    @DeleteMapping("/deleteByIdParam") //http://localhost:8080/contactMessages/deleteByIdParam?contactMessageId=1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<String> deleteById(@RequestParam(value = "contactMessageId") Long contactMessageId ){

        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

    // requestparamda farki istedigimiz keydegeri endpoint'te yazili olarak gelmeyecek.
     // PathVariable ile calisiyorsak {contactMessageId} path'ten sonraki kisima metohd'un icerisindeki field'in ismi süslü parantez icerisinde yazilmalidir.
    // Not: deleteByIdWithPath *********************************
    @DeleteMapping("/deleteById/{contactMessageId}") //http://localhost:8080/contactMessages/deleteById/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<String> deleteByIdPath(@PathVariable Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

    // Not: getByIdParam ****************************************
    @GetMapping("/getByIdParam") // http://localhost:8080/contactMessages/getByIdParam?contactMessageId=1 + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<ContactMessage>  getById(@RequestParam(value = "contactMessageId") Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }


    // Not: getByIdPath *********************************************
    @GetMapping("/getById/{contactMessageId}") // http://localhost:8080/contactMessages/getById/1  + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<ContactMessage> getByIdPath(@PathVariable Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }

    // Not: updateById ******************************************
     // Long contactMessageId hangi objenin update edilecegi alindi.
     // @RequestBody bir Json ile client'an gerekli verilerin alinmasi saglanir
    // alinan bu Json'in tamamen null olarak gelmemesi icin iclerinden 1 tanede olsa bir verinin dolu olmasi icin @NotNull kullanilir.
    // gelen Json'i   ContactMessageUpdateRequest classi karsilar.
    @PutMapping("/updateById/{contactMessageId}") // http://localhost:8080/contactMessages/updateById/1  + PUT + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<ResponseMessage<ContactMessageResponse>> updateById(@PathVariable Long contactMessageId ,
                                                                              @RequestBody @NotNull ContactMessageUpdateRequest contactMessageUpdateRequest) {
        return ResponseEntity.ok(contactMessageService.updateById(contactMessageId, contactMessageUpdateRequest));
    }


    // Not: Odev --> searchByDateBetween ************************
     // Burada Page Yapıda kullanılabilirdi ancak list yapıyı kullandığımızda dönem değer olarak boş gelmesi durumunda herhangi bir hata almayiz.
     // Bir Veri alacak olsaydık pathVarialble kullanırdık ancak burada birden çok veri alacağımızdan dolayı RequestParam kullanilmistir
    @GetMapping("/searchBetweenDates") // http://localhost:8080/contactMessages/searchBetweenDates?beginDate=2023-09-13&endDate=2023-09-15
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<List<ContactMessage>> searchByDateBetween(
            @RequestParam(value = "beginDate") String beginDateString,
            @RequestParam(value = "endDate") String endDateString){
        List<ContactMessage>contactMessages = contactMessageService.searchByDateBetween(beginDateString, endDateString);
        return ResponseEntity.ok(contactMessages);
    }

    // Not: Odev --> searchByTimeBetween ************************
    @GetMapping("/searchBetweenTimes") // http://localhost:8080/contactMessages/searchBetweenTimes?startHour=09&startMinute=00&endHour=17&endMinute=30
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<List<ContactMessage>> searchByTimeBetween(
            @RequestParam(value = "startHour") String startHour,
            @RequestParam(value = "startMinute") String startMinute,
            @RequestParam(value = "endHour") String endHour,
            @RequestParam(value = "endMinute") String endMinute){
        List<ContactMessage>contactMessages = contactMessageService.searchByTimeBetween(startHour,startMinute,endHour,endMinute);
        return ResponseEntity.ok(contactMessages);
    }

}