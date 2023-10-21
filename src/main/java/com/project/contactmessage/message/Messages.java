package com.project.contactmessage.message;

public class Messages {


    public static final String NOT_FOUND_MESSAGE="Message not found";

    public static final String CONTACT_MESSAGE_DELETE="Contact Message deleted successfully";

    public static final String WRONG_DATE_FORMAT = "Wrong Date Format";

    public static final String WRONG_TIME_FORMAT = "Wrong Time Format";
}
// Burda static code'lu ve String tipinde final message'lar olusturduk bunu static yapmamizin sebebi
// bu messagi cagirirken service katinda new'lemek zorunda kalmayalim diye. Static methodlara ve fieldlara class ismini yazarak ulasabiliriz.