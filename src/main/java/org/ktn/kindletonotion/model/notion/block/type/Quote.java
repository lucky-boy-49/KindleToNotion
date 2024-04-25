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

    /**
     * 页子项类型
     */
    private String object = "block";

    /**
     * 块类型
     */
    private String type = "quote";

    /**
     * 笔记位置
     */
    private ObjectNode quote;

    /**
     * json mapper
     */
    @JsonIgnore
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 构造函数
     * 例子：
     * {
     *   "object":"block",
     *   "type":"quote",
     *   "quote":{
     *     "rich_text":[
     *       {
     *         "type":"text",
     *         "text":{
     *           "content":{@param content}
     *         }
     *       }
     *     ]
     *   }
     * }
     * @param content 内容
     */
    public Quote(String content) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        rootNode.put("color", "yellow");
        this.quote = rootNode;
    }

}
