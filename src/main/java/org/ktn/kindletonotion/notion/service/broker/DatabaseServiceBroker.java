package org.ktn.kindletonotion.notion.service.broker;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.Exception.NotionResponseException;
import org.ktn.kindletonotion.model.NotionReact;
import org.ktn.kindletonotion.notion.model.Database;
import org.ktn.kindletonotion.notion.model.page.PageData;
import org.ktn.kindletonotion.notion.service.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Objects;

/**
 * Database操作
 * @author 贺佳
 */
@Slf4j
@Service
public class DatabaseServiceBroker {

    private final WebClient client;

    public DatabaseServiceBroker(WebClient client) {
        this.client = client;
    }

    /**
     * 查询数据库
     * @param databaseId 数据库id
     * @return 数据库数据
     */
    public NotionReact<List<PageData>> queryPages(String databaseId) {
        log.info("查询Notion数据库数据");
        try {
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
            DatabaseService service = factory.createClient(DatabaseService.class);
            ResponseEntity<Database> response = service.queryPages(databaseId);
            return new NotionReact<>(response.getStatusCode().value(), "查询Notion数据库数据成功", Objects.requireNonNull(response.getBody()).getPageDataList());
        } catch (NotionResponseException e) {
            log.error("查询Notion数据库数据失败，错误码：{}，错误信息：{}", e.getCode(), e.getMessage());
            return new NotionReact<>(e.getCode(), "查询Notion数据库数据失败", null);
        } catch (Exception e) {
            log.error("查询Notion数据库数据失败，错误信息：{}", e.getMessage());
            return new NotionReact<>(0, "查询Notion数据库数据失败", null);
        }
    }

}
