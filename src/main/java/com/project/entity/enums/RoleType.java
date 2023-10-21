package com.project.entity.enums;

public enum RoleType {

    ADMIN("Admin"),
    TEACHER("Teacher"),
    STUDENT("Student"),
    MANAGER("Dean"),
    ASSISTANT_MANAGER("ViceDean");

    // Bu degisken üzerinden Enumlara ulasacagiz
    public final String name;

   // Enum'larin icersindeki String yapilarin setlenmesi icin; artik uygulama icerisinden bu condtructor sayesinde enum tipli rollere ulasabilecegiz.
    RoleType(String name) {
        this.name = name;
    }

    public String getName(){
        return  name;
    }
}
// ADMIN("Admin") yazmamdaki neden bu enum'a string bir ifade ile ulasmak istememdir.
// ADMIN("Admin") Uygulama icerisinde Enum type'deki bir yapiya burda ADMIN'ne Admin gibi bir string ile ulasmak istiyorsak
// bu icteki yapilari yazmaliyiz bunlar ayni olmak zorunda degil, burda oldugu gibi--> MANAGER("Dean")

// Burda asil amac stringleri kullanmamizin sebebi repositoriy katmaninda bu rollere biz string olarak ulasacagiz
// bunlar bize zaten string olarak gelecek ve bize ekstra birsey yapmamiza gerek olmayacak.
