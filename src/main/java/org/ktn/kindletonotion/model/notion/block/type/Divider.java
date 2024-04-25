package org.ktn.kindletonotion.model.notion.block.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * 横线所使用的类型
 * @author jiahe
 */
@Data
public class Divider {

    private String object = "block";

    private String type = "divider";

    private Object divider;

    public Divider() {
        ObjectMapper mapper = new ObjectMapper();
        this.divider = mapper.createObjectNode();
    }

}
