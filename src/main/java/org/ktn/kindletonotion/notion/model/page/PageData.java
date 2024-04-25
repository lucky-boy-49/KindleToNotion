package org.ktn.kindletonotion.notion.model.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 贺佳
 * notion页面模型
 */
@Data
public class PageData {

    private String object;

    private String id;

    @JsonProperty("created_time")
    private LocalDateTime createdTime;

    @JsonProperty("last_edited_time")
    private LocalDateTime lastEditedTime;

    private JsonNode cover;

    private JsonNode icon;

    private Boolean archived;

    private String url;

    private JsonNode properties;

}
