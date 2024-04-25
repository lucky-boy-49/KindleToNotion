package org.ktn.kindletonotion.model.notion.block.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * 横线所使用的类型
 * @author jiahe
 */
@Data
public class Divider {

    /**
     * 页子项类型
     */
    private String object = "block";

    /**
     * 块类型
     */
    private String type = "divider";

    /**
     * 横线
     */
    private Object divider;

    /**
     * 构造函数
     * 例子：
     * {
     *     "divider": {}
     * }
     */
    public Divider() {
        ObjectMapper mapper = new ObjectMapper();
        this.divider = mapper.createObjectNode();
    }

}
