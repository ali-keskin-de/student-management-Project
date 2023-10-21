package com.project.contactmessage.repository;

import com.project.contactmessage.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Repository interface'i  DB ye veriyi göndermek ve DB'den verileri almak icin kullanilan son katman.
// @Repository bunu zorunlu degil ancak kod okunurlulugunu artirir biz bu interface JpaRepository(generic bir class'tir)
// ile extends ettikten sonra Spring bunun repository oldugunu anlar.

@Repository
public interface ContactMessageRepository extends JpaRepository <ContactMessage, Long> {
     //Biz asagida türetigimiz metohdu Jpa göre yaparsak query yazmamiza gerek kalmaz, findById biz gene bizim pojo class'imizda olan  email field'i yazdigimizda
    // (burda kamelcase dikat etmeliyiz) bun metohd'u query'sini yazmaya gerek birakmada jpa bize sunar. Dönen datanin type bize belirlememize olanak tanimaktadir Data Jpa;
    // asagida oldugu gibi eger bir Page yapisinda dönmesini istiyorsak metohd'un parametresine asagidaki gibi  Pageable pageable yazmaliyiz.
    // burda dikkat edilmesi gerekli olan yerlerden biride Class'lar dogru yerlerden import edilmelidir.
    // Controller tarafinda bu method tetiklendiginde istedigimiz data gelmez ise iki ihtimal vardir;
    // 1) Ya service tarafinda yaptigimiz dizayn yanlis yada
    // 2) Yada türetilen method yanlis keyword'lari yanlis kullandiniz. Burdaki method'un yanlis oldugunu söyle anlariz arka tarafda biz show-sql true cekmistik bir orda query görecegiz.Ordan kontroller saglana bilir.

    Page<ContactMessage> findByEmailEquals(String email, Pageable pageable);

    // ContactMessage icerisine pojo olan class geliyor. DB ile calistigimizdan dolayi bize pojo gelir
    // ve biz bunu burdada DTO dönüstürebiliriz ancak bu istenen bir durum degildir bu tarz islemler service katinda yapilir.
    // Asagidaki sorguda sunu demis oluyoruz bana parametredeki subject degeri ile ayni olan objeleri getir,
    // ancak bunlari Pageable yapida getir.
    Page<ContactMessage> findBySubjectEquals(String subject, Pageable pageable);

     /*
        FUNCTION('DATE', c.dateTime): Bu bölüm, c.dateTime alanının tarih bileşenini çıkarmak için kullanılır.
        c.dateTime bir tarih-saat nesnesi içerdiğinden ve bu sorgu yalnızca tarih bileşenini kullanmak istediğinden,
        FUNCTION işlevi ile tarih bileşeni çıkarılır.
     */
// FUNCTION legonun istenilen parcasini almaya yariyor bunun alternatifleride var.
    @Query("select c from ContactMessage c where FUNCTION('DATE', c.dateTime) between ?1 and ?2")
    List<ContactMessage> findMessagesBetweenDates(LocalDate beginDate, LocalDate endDate);

    /* !!!!
"(EXTRACT(HOUR FROM c.dateTime) BETWEEN :startHour AND :endHour) AND "--> Bununla gelen saat bilgisi startHour ile endHour arasinda mi eger bu dogruysa
 "(EXTRACT(HOUR FROM c.dateTime) != :startHour OR EXTRACT(MINUTE FROM c.dateTime) >= :startMinute)--> baslama saati esit degilse
 gelen zaman bilgisi o zaman dakikaya bak baslama dakikasindan büyük mü? büyük ise ok
 "(EXTRACT(HOUR FROM c.dateTime) != :endHour OR EXTRACT(MINUTE FROM c.dateTime) <= :endMinute)"--> bitis saatine esit mi
 gelen saat bilgisi esit degilse o zaman bittis dakika bak eger kücükse ok
 */
    @Query("SELECT c FROM ContactMessage c WHERE " +
            "(EXTRACT(HOUR FROM c.dateTime) BETWEEN :startHour AND :endHour) AND " +
            "(EXTRACT(HOUR FROM c.dateTime) != :startHour OR EXTRACT(MINUTE FROM c.dateTime) >= :startMinute) AND " +
            "(EXTRACT(HOUR FROM c.dateTime) != :endHour OR EXTRACT(MINUTE FROM c.dateTime) <= :endMinute)")
    List<ContactMessage> findMessagesBetweenTimes(@Param("startHour") int startHour,
                                                  @Param("startMinute") int startMinute,
                                                  @Param("endHour") int endHour,
                                                  @Param("endMinute") int endMinute);

}