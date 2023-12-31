package org.ktn.kindletonotion.notion.service;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.model.Database;
import org.ktn.kindletonotion.notion.model.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @author 贺佳
 * Database操作
 */
@Slf4j
@Service
public class DatabaseService {

    private final NotionConfigProperties notionConfigProps;

    private final RestTemplate restTemplate;

    public DatabaseService(NotionConfigProperties notionConfigProps, RestTemplate restTemplate) {
        this.notionConfigProps = notionConfigProps;
        this.restTemplate = restTemplate;
    }

    public List<Page> query(String databaseId) {

        String url = notionConfigProps.apiUrl() + "/v1/databases/" + databaseId + "/query";
        log.info("查询Notion数据库：{}", url);

        ResponseEntity<Database> db = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(getDefaultHeaders()),
                Database.class
        );

        return Objects.requireNonNull(db.getBody()).getPages();

    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Notion-Version", notionConfigProps.apiVersion());
        headers.set("Authorization", notionConfigProps.authToken());
        return headers;
    }

}
