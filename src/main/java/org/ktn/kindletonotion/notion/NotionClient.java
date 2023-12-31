package org.ktn.kindletonotion.notion;

import org.ktn.kindletonotion.notion.service.DatabaseService;

/**
 * @author 贺佳
 * Notion客户端
 */
public class NotionClient {

    private final DatabaseService database;

    public NotionClient(DatabaseService database) {
        this.database = database;
    }



}
