package org.ktn.kindletonotion.notion.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象转化为Json工具
 * @author jiahe
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("对象转换Json失败", e);
        }
        return "";
    }

    public static ArrayNode getRichText(String content) {
        ArrayNode richText = MAPPER.createArrayNode();
        ObjectNode text = MAPPER.createObjectNode();
        ObjectNode con = MAPPER.createObjectNode();
        con.put("content", content);
        text.put("type", "text");
        text.set("text", con);
        richText.add(text);
        return richText;
    }

    public static ObjectNode getIcon(String type, String value) {
        ObjectNode icon = MAPPER.createObjectNode();
        icon.put("type", type);
        icon.put(type, value);
        return icon;
    }

}
