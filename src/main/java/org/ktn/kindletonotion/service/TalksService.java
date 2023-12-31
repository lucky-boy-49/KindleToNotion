package org.ktn.kindletonotion.service;

import org.ktn.kindletonotion.model.Talk;
import org.ktn.kindletonotion.notion.model.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 贺佳
 */
@Service
public class TalksService {

    public static Talk mapPageToTalk(Page page) {
        return new Talk(
                page.getId(),
                page.getProperties().get("Title").get("title").get(0).get("text").get("content").asText(),
                LocalDateTime.parse(page.getProperties().get("Last Edited").get("last_edited_time").asText(), DateTimeFormatter.ISO_DATE_TIME),
                LocalDate.parse(page.getProperties().get("Last Highlighted").get("date").get("start").asText(), DateTimeFormatter.ISO_DATE),
                LocalDate.parse(page.getProperties().get("Last Synced").get("date").get("start").asText(), DateTimeFormatter.ISO_DATE),
                page.getProperties().get("Author").get("rich_text").get(0).get("text").get("content").asText(),
                page.getUrl()
        );
    }

}
