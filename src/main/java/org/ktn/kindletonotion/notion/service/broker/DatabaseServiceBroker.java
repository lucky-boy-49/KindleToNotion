package org.ktn.kindletonotion.notion.service.broker;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.Exception.NotionResponseException;
import org.ktn.kindletonotion.model.NotionReact;
import org.ktn.kindletonotion.notion.model.Database;
import org.ktn.kindletonotion.notion.service.DatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Objects;

/**
 * Database操作
 * @author 贺佳
 */
@Slf4j
@Service
public class DatabaseServiceBroker {

    private final HttpServiceProxyFactory factory;

    public DatabaseServiceBroker(HttpServiceProxyFactory factory) {
        this.factory = factory;
    }

    /**
     * 查询数据库
     * @param databaseId 数据库id
     * @return 数据库数据
     */
    public NotionReact<Object> queryPages(String databaseId) {
        try {
            log.info("查询Notion数据库数据");
            DatabaseService service = factory.createClient(DatabaseService.class);
            ResponseEntity<Database> response = service.queryPages(databaseId);
            log.info("查询Notion数据库数据成功");
            return new NotionReact<>(response.getStatusCode().value(), "查询Notion数据库数据成功", Objects.requireNonNull(response.getBody()).getPageDataList());
        } catch (NotionResponseException e) {
            log.error("查询Notion数据库数据失败，错误码：{}，错误信息：{}", e.getCode(), e.getMessage());
            return new NotionReact<>(e.getCode(), "查询Notion数据库数据失败", e.getMessage());
        } catch (Exception e) {
            log.error("查询Notion数据库数据失败，错误信息：{}", e.getMessage());
            return new NotionReact<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "查询Notion数据库数据失败", e.getMessage());
        }
    }

}
