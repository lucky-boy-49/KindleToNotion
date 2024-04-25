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

    /**
     * 页子项类型
     */
    private String object = "block";

    /**
     * 块类型
     */
    private String type = "callout";

    /**
     * 标注
     */
    private ObjectNode callout;

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
     *   "type":"callout",
     *   "callout":{
     *     "rich_text":[
     *       {
     *         "type":"text",
     *         "text":{
     *           "content":@content
     *         }
     *       }
     *     ]
     *   }
     * }
     * @param content 内容
     */
    public Callout(String content) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        this.callout = rootNode;
    }

    /**
     * 构造函数
     * 例子：
     * {
     *   "object":"block",
     *   "type":"callout",
     *   "callout":{
     *     "rich_text":[
     *       {
     *         "type":"text",
     *         "text":{
     *           "content":@content
     *         }
     *       }
     *     ],
     *     "icon":{
     *       "type":"emoji",
     *       "emoji":"?"
     *     },
     *     "color":@color
     *   }
     * }
     * @param content 内容
     * @param color 颜色
     */
    public Callout(String content, String color) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("rich_text", JsonUtil.getRichText(content));
        rootNode.set("icon", JsonUtil.getIcon("emoji", "\uD83D\uDCA1"));
        rootNode.put("color", color);
        this.callout = rootNode;
    }

}
