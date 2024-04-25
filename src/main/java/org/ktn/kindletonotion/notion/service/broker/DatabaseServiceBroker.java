package org.ktn.kindletonotion.notion.service.broker;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.model.Database;
import org.ktn.kindletonotion.notion.model.page.PageData;
import org.ktn.kindletonotion.notion.service.DatabaseService;
import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
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

    private final NotionConfigProperties notionConfigProps;


    private final HttpHeaderUtil httpHeaderUtil;

    public DatabaseServiceBroker(NotionConfigProperties notionConfigProps, HttpHeaderUtil httpHeaderUtil) {
        this.notionConfigProps = notionConfigProps;
        this.httpHeaderUtil = httpHeaderUtil;
    }

    /**
     * 查询数据库
     * @param databaseId 数据库id
     * @return 数据库数据
     */
    public List<PageData> queryPages(String databaseId) {
        log.info("查询Notion数据库数据");
        WebClient client = WebClient.builder().baseUrl(notionConfigProps.apiUrl()).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        DatabaseService service = factory.createClient(DatabaseService.class);
        ResponseEntity<Database> response = service.queryPages(databaseId, httpHeaderUtil.getDefaultHeaders());
        return Objects.requireNonNull(response.getBody()).getPageDataLIst();
    }

}
