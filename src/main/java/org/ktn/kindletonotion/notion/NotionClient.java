package org.ktn.kindletonotion.notion;

import org.ktn.kindletonotion.notion.service.BlockService;
import org.ktn.kindletonotion.notion.service.DatabaseService;
import org.ktn.kindletonotion.notion.service.PageService;
import org.springframework.stereotype.Component;

/**
 * @author 贺佳
 * Notion客户端
 */
@Component
public class NotionClient {

    public final DatabaseService database;

    public final PageService page;

    public final BlockService block;

    public NotionClient(DatabaseService database, PageService page, BlockService block) {
        this.database = database;
        this.page = page;
        this.block = block;
    }



}
