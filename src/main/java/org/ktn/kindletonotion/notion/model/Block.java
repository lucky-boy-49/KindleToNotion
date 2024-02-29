package org.ktn.kindletonotion.notion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 页面中的子项
 * @author 贺佳
 */
@Data
public class Block {

    private String object;

    private String id;

    private JsonNode parent;

    @JsonProperty("created_time")
    private LocalDateTime createdTime;

    @JsonProperty("last_edited_time")
    private LocalDateTime lastEditedTime;

    private String type;

    private JsonNode callout;

    private JsonNode quote;

    private JsonNode paragraph;

    private JsonNode divider;

}
