package org.ktn.kindletonotion.model.notion.block.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.ktn.kindletonotion.notion.utils.JsonUtil;

/**
 * 标记所使用的类型
 * @author jiahe
 */
@Data
public class Callout {

    private String object = "block";

    private String type = "callout";

    private ObjectNode callout;

    @JsonIgnore
    private ObjectMapper mapper = new ObjectMapper();

    public Callout(String content) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        this.callout = rootNode;
    }

    public Callout(String content, String color) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        rootNode.set("icon", JsonUtil.getIcon("emoji", "\uD83D\uDCA1"));
        rootNode.put("color", color);
        this.callout = rootNode;
    }

}
