package org.ktn.kindletonotion.model.notion.block.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.ktn.kindletonotion.notion.utils.JsonUtil;

/**
 * 笔记位置所使用的类型
 * @author jiahe
 */
@Data
public class Paragraph {

    private String object = "block";

    private String type = "paragraph";

    private ObjectNode paragraph;

    @JsonIgnore
    private ObjectMapper mapper = new ObjectMapper();

    public Paragraph(String content) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        this.paragraph = rootNode;
    }

    public Paragraph(String content, String color) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        this.paragraph = rootNode;
        this.paragraph.put("color", color == null ? "default" : color);
    }

}
