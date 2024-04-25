package org.ktn.kindletonotion.model.notion.block.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.ktn.kindletonotion.notion.utils.JsonUtil;

/**
 * 评论所使用的类型
 * @author jiahe
 */
@Data
public class Quote {

    private String object = "block";

    private String type = "quote";

    private ObjectNode quote;

    @JsonIgnore
    private final ObjectMapper mapper = new ObjectMapper();

    public Quote(String content) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        rootNode.put("color", "yellow");
        this.quote = rootNode;
    }

}
