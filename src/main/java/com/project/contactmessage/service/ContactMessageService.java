package com.project.contactmessage.service;


import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.dto.ContactMessageUpdateRequest;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.exception.ConflictException;
import com.project.contactmessage.exception.ResourceNotFoundException;
import com.project.contactmessage.mapper.ContactMessageMapper;
import com.project.contactmessage.message.Messages;
import com.project.contactmessage.repository.ContactMessageRepository;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
// @RequiredArgsConstructor bunu constructor enjektion yapmak icin ekliyoruz
// daha sonra enjektion yapmak istedigimiz class veya interface final keywordu ile enjekte ediyoruz asagida görüldügü gibi.
//@Service bunun service oldugunu belirtmis oluyoruz.
@Service
@RequiredArgsConstructor
public class ContactMessageService {

    // Burada @RequiredArgsConstructor sayesinde constructor injection yaptik.
    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    // Not: save() **************************************************
    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {

        // burada DB kayit etmeden önce ellimizdeki Pojo tipinde olmayan class'i önce pojoya dönüstürüp öyle kayit etmeliyiz.
        // Bunu yapan Kütüphaneler olsada IT dünyasinda yaygin olan method elle yapmaktir bizde bunu mapper classlari olusturarak yaptik.
        // Mapplemeleri yapan Kütüphanelere örnek : ModelMapper ve MapStruct ...
        // Ancak bu kütüphaneler calisildiginda bu kütüphanelere bagimli kalmis oluyorsunuz. Ve bazen Entity class'ina bir field eklendiginde hemmen mapleyemiyor.

        ContactMessage contactMessage = contactMessageMapper.requestToContactMessage(contactMessageRequest);
        ContactMessage savedData =  contactMessageRepository.save(contactMessage);

        // Save() DB kaydettigi nesnenin kendisini return etmektedir.
        // contactMessage ile savedData nesnesi arasindaki fark id dir nesne DB olusturulurken otomatik olarak id generate edildi.

        // ResponseMessage.<ContactMessageResponse>builder() yazdiktan sonra bir bosluk birakarak . deyip fieldlari yaziyoruz böylelikle build() methodu'u getirmekten kurtuluyoruz. Yoksa build() methodu geliyor.

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Created Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(contactMessageMapper.contactMessageToResponse(savedData))
                .build();

    }

    // Not: getAll() *************************************************
    public Page<ContactMessageResponse> getAll(int page, int size, String sort, String type) {

       // Page data tipinden Pageable daata tipine gecmemizin sebebi cünkü Data Jpa hazir methodlari pageable nesnesini parametre olarak olmaktadir.
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")){
             pageable = PageRequest.of(page,size, Sort.by(sort).descending());
        }

        // findAll(pageable) methodu bize pojolar getiriyor. map() stream 'den gelmiyor ve mapper class'lar yardimiyla pojolari dönüstürmeyi sagliyor.
        return contactMessageRepository.findAll(pageable).map(contactMessageMapper::contactMessageToResponse);
    }

    // Not: searchByEmail ***************************************
    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")){
            pageable = PageRequest.of(page,size, Sort.by(sort).descending());
        }
        // DB ile calisiyorsak bize Pojo gelecek biz bunu client'a döneceksek mutlaka Response dönüstürmeliyiz. Bu service katinda yapilir.
        return contactMessageRepository.findByEmailEquals(email,pageable)
                .map(contactMessageMapper::contactMessageToResponse);
    }

    // Not: searchBySubject *************************************
    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")){
            pageable = PageRequest.of(page,size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findBySubjectEquals(subject,pageable)
                .map(contactMessageMapper::contactMessageToResponse);

    }

    // Not: getById *********************************************
    public ContactMessage getContactMessageById(Long contactMessageId) {

      return  contactMessageRepository.findById(contactMessageId).orElseThrow(
                ()-> new ResourceNotFoundException(Messages.NOT_FOUND_MESSAGE));
    }

    // Not: deleteByIdParam *************************************
    public String deleteById(Long contactMessageId) {
         // burda entity üzerinden sildik.
        // contactMessageRepository.delete(getContactMessageById(contactMessageId));

        getContactMessageById(contactMessageId);
        contactMessageRepository.deleteById(contactMessageId);
        return Messages.CONTACT_MESSAGE_DELETE;
    }


    // Not: updateById ******************************************
    public ResponseMessage<ContactMessageResponse> updateById(Long id, ContactMessageUpdateRequest contactMessageUpdateRequest) {
        // Gelen Id li message var mi? kontrollu yapiliyor.
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

    // Not: Odev --> searchByDateBetween ************************
    public List<ContactMessage> searchByDateBetween(String beginDateString, String endDateString) {

        // Burda try catch icerisine alma nedenimiz gelecek
        // string tipindeki data LocalDate dönüstürülemeyecek bir yapida ise o zaman catch calisacak.
        try {
            LocalDate beginDate = LocalDate.parse(beginDateString);
            LocalDate endDate = LocalDate.parse(endDateString);
            return contactMessageRepository.findMessagesBetweenDates(beginDate, endDate);
        } catch (DateTimeParseException e) {
            throw new ConflictException(Messages.WRONG_DATE_FORMAT);
        }
    }


    // Not: Odev --> searchByTimeBetween ************************
    public List<ContactMessage> searchByTimeBetween(String startHourString, String startMinuteString,
                                                    String endHourString, String endMinuteString) {

        // Burda try catch icerisine alma nedenimiz gelecek
        // string tipindeki data int dönüstürülemeyecek bir yapida ise o zaman catch calisacak.
        try {
            int startHour = Integer.parseInt(startHourString);
            int startMinute = Integer.parseInt(startMinuteString);
            int endHour = Integer.parseInt(endHourString);
            int endMinute = Integer.parseInt(endMinuteString);
            return contactMessageRepository.findMessagesBetweenTimes(startHour, startMinute, endHour, endMinute);
        } catch (NumberFormatException e) {
            throw new ConflictException(Messages.WRONG_TIME_FORMAT);
        }
    }
}