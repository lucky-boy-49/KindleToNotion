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

    /**
     * 书名
     */
    @JsonProperty("Title")
    private ObjectNode title;

    /**
     * 作者
     */
    @JsonProperty("作者")
    private  ObjectNode author;

    /**
     * 笔记数
     */
    @JsonProperty("笔记数")
    private ObjectNode markNum;

    /**
     * 最后标记
     */
    @JsonProperty("最后标记")
    private ObjectNode lastMark;

    /**
     * 上次同步
     */
    @JsonProperty("上次同步")
    private ObjectNode lastSynced;

    /**
     * 读书状态
     */
    @JsonProperty("读书状态")
    private ObjectNode readingStatus;

}
