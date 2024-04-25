package org.ktn.kindletonotion.notion.model.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 页码属性
 * @author jiahe
 */
@Data
public class PageProperties {

    /**
     * 属性数据
     */
    private ObjectNode properties;

    /**
     * 构造方法，传入笔记数和最后标记日期，用于更新页面数据请求
     * @param makeNum 笔记数
     * @param markDate 最后标记日期
     */
    public PageProperties(int makeNum, String markDate) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        // 更新笔记数
        ObjectNode numberOfNotes = mapper.createObjectNode();
        numberOfNotes.put("number", makeNum);
        rootNode.set("笔记数", numberOfNotes);
        // 更新最后标记时间
        ObjectNode lastMarkDate = mapper.createObjectNode();
        ObjectNode date = mapper.createObjectNode();
        date.set("start", date);
        lastMarkDate.put("date", markDate);
        rootNode.set("最后标记", lastMarkDate);
        // 更新上次同步
        ObjectNode lastSynced = mapper.createObjectNode();
        date = mapper.createObjectNode();
        date.put("start", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        lastSynced.set("date", date);
        rootNode.set("上次同步", lastSynced);
        // 更新请求体
        this.setProperties(rootNode);
    }

}
