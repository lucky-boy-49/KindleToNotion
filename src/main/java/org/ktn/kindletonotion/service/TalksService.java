package org.ktn.kindletonotion.service;

import org.ktn.kindletonotion.model.Talk;
import org.ktn.kindletonotion.notion.model.page.PageData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 贺佳
 */
@Service
public class TalksService {

    public static Talk mapPageToTalk(PageData pageData) {
        return new Talk(
                pageData.getId(),
                pageData.getProperties().get("Title").get("title").get(0).get("text").get("content").asText(),
                LocalDateTime.parse(pageData.getProperties().get("最后编辑").get("last_edited_time").asText(), DateTimeFormatter.ISO_DATE_TIME),
                LocalDate.parse(pageData.getProperties().get("最后标记").get("date").get("start").asText(), DateTimeFormatter.ISO_DATE),
                LocalDate.parse(pageData.getProperties().get("上次同步").get("date").get("start").asText(), DateTimeFormatter.ISO_DATE),
                pageData.getProperties().get("作者").get("rich_text").get(0).get("text").get("content").asText(),
                pageData.getUrl()
        );
    }

}
