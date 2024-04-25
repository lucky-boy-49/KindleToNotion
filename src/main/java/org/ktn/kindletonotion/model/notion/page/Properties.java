package org.ktn.kindletonotion.model.notion.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

/**
 * 创建页面时的页面属性
 * @author jiahe
 */
@Data
public class Properties {

    @JsonProperty("Title")
    private ObjectNode title;

    @JsonProperty("作者")
    private  ObjectNode author;

    @JsonProperty("笔记数")
    private ObjectNode markNum;

    @JsonProperty("最后标记")
    private ObjectNode lastMark;

    @JsonProperty("上次同步")
    private ObjectNode lastSynced;

    @JsonProperty("读书状态")
    private ObjectNode readingStatus;

}
