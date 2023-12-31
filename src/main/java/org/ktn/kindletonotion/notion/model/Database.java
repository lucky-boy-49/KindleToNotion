package org.ktn.kindletonotion.notion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 贺佳
 * 数据库模型
 */
@Data
public class Database {

    private String object;

    @JsonProperty("results")
    private List<Page> pages = new ArrayList<>();

    @JsonProperty("next_cursor")
    private String nextCursor;

    @JsonProperty("has_more")
    private Boolean hasMore;


}
