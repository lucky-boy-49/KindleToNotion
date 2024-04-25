package org.ktn.kindletonotion.model.notion.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.ktn.kindletonotion.notion.utils.JsonUtil;

/**
 * 创建页面时使用的模型
 * @author jiahe
 */
@Data
public class Page {

    /**
     * 父id
     */
    private ObjectNode parent;

    /**
     * 页属性
     */
    private Properties properties;

    public Page(String databaseId) {
        ObjectMapper mapper = JsonUtil.getMapper();
        parent =  mapper.createObjectNode();
        parent.put("database_id", databaseId);
    }

}
