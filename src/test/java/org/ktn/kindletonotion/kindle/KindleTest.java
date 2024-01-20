package org.ktn.kindletonotion.kindle;

import org.junit.jupiter.api.Test;
import org.ktn.kindletonotion.kindle.service.KindleService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class KindleTest {

    @Test
    public void parseNotesTest() {
        KindleService kindleService = new KindleService();
        kindleService.parseNotes("G:\\IdeaProjects\\KindleToNotion\\src\\main\\resources\\My Clippings.txt");
    }

    @Test
    public void timeTest() {

        String dateString = "2023年11月3日星期一 下午7:46:43";
//        String pattern = "yyyy年MM月dd日E ah:mm:ss";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.CHINESE);
//        LocalDateTime markTime = LocalDateTime.parse(dateString, formatter);
//        System.out.println(markTime);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy年MM月dd日E ahh:mm:ss");
        try {
            Date parse = inputFormat.parse(dateString);
            LocalDateTime localDateTime = parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            System.out.println(localDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
