package org.ktn.kindletonotion.notion.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ktn.kindletonotion.kindle.model.Book;
import org.ktn.kindletonotion.model.notion.page.Properties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * notion工具
 * @author jiahe
 */
public class NotionUtil {

    public static Properties getProperties(Book book) {
        Properties properties = new Properties();
        ObjectMapper mapper = JsonUtil.getMapper();

        // 标题
        ObjectNode title = mapper.createObjectNode();
        ObjectNode text = mapper.createObjectNode();
        text.put("content", book.getName());
        title.set("title", text);
        properties.setTitle(title);

        // 作者
        ObjectNode author = mapper.createObjectNode();
        ArrayNode richText = mapper.createArrayNode();
        text = mapper.createObjectNode();
        text.put("content", book.getAuthor());
        author.set("rich_text", richText);
        properties.setAuthor(author);

        // 笔记数
        ObjectNode numberOfNotes = mapper.createObjectNode();
        numberOfNotes.put("number", 0);
        properties.setMarkNum(numberOfNotes);

        // 最后标记
        ObjectNode lastMarked = mapper.createObjectNode();
        ObjectNode date = mapper.createObjectNode();
        date.put("start", LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        lastMarked.set("date", date);
        properties.setLastMark(lastMarked);

        // 上次同步
        ObjectNode lastSynced = mapper.createObjectNode();
        date = mapper.createObjectNode();
        date.put("start", LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        lastSynced.set("date", date);
        properties.setLastSynced(lastSynced);

        // 读书状态
        ObjectNode readingStatus = mapper.createObjectNode();
        ObjectNode select = mapper.createObjectNode();
        select.put("id", "Hld^");
        select.put("name", "在读");
        select.put("color", "red");
        readingStatus.set("select", select);
        properties.setReadingStatus(readingStatus);

        return properties;
    }

}
