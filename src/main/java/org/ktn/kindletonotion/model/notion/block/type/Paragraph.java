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

    /**
     * 页子项类型
     */
    private String object = "block";

    /**
     * 块类型
     */
    private String type = "paragraph";

    /**
     * 段
     */
    private ObjectNode paragraph;

    /**
     * json mapper
     */
    @JsonIgnore
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 构造函数
     * 例子：
     * {
     *   "object":"block",
     *   "type":"paragraph",
     *   "paragraph":{
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
    public Paragraph(String content) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        this.paragraph = rootNode;
    }

    /**
     * 构造函数
     * 例子：
     * {
     *   "object":"block",
     *   "type":"paragraph",
     *   "paragraph":{
     *     "rich_text":[
     *       {
     *         "type":"text",
     *         "text":{
     *           "content":"test"
     *         }
     *       }
     *     ],
     *     "color":"default"
     *   }
     * }
     * @param content 内容
     * @param color 颜色
     */
    public Paragraph(String content, String color) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        this.paragraph = rootNode;
        this.paragraph.put("color", color.isEmpty() ? "default" : color);
    }

}
