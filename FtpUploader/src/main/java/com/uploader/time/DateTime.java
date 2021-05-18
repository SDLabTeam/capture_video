package com.uploader.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTime {

    public static String getCurrentFolder(LocalDateTime localDateTime) {
        return getCurrentFolder(localDateTime, "yyyy-MM");
    }

    public static String getCurrentFolder(LocalDateTime localDateTime, String format){
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime getCurrentTime(){
        return LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
    }
}
