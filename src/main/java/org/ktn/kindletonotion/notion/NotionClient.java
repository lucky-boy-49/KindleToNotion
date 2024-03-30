package org.ktn.kindletonotion.notion;

import org.ktn.kindletonotion.notion.service.broker.BlockServiceBroker;
import org.ktn.kindletonotion.notion.service.broker.DatabaseServiceBroker;
import org.ktn.kindletonotion.notion.service.broker.PageServiceBroker;
import org.springframework.stereotype.Component;

/**
 * @author 贺佳
 * Notion客户端
 */
@Component
public class NotionClient {

    public final DatabaseServiceBroker database;

    public final PageServiceBroker page;

    public final BlockServiceBroker block;

    public NotionClient(DatabaseServiceBroker database, PageServiceBroker page, BlockServiceBroker block) {
        this.database = database;
        this.page = page;
        this.block = block;
    }



}
