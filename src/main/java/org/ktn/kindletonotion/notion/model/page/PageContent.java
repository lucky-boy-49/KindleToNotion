package org.ktn.kindletonotion.notion.model.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.ktn.kindletonotion.notion.model.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * 页的内容
 * @author 贺佳
 */
@Data
public class PageContent {

    private String object;

    @JsonProperty("results")
    private List<Block> blockList = new ArrayList<>();

    @JsonProperty("next_cursor")
    private String nextCursor;

    @JsonProperty("has_more")
    private Boolean hasMore;

}
