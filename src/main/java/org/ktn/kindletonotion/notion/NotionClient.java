package org.ktn.kindletonotion.notion;

import org.ktn.kindletonotion.notion.service.DatabaseService;
import org.springframework.stereotype.Component;

/**
 * @author 贺佳
 * Notion客户端
 */
@Component
public class NotionClient {

    public final DatabaseService database;

    public NotionClient(DatabaseService database) {
        this.database = database;
    }



}
