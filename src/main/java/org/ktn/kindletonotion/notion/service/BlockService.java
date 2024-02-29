package org.ktn.kindletonotion.notion.service;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class BlockService {

    private final NotionConfigProperties notionConfigProps;

    private final RestTemplate restTemplate;

    private final HttpHeaderUtil httpHeaderUtil;

    public BlockService(NotionConfigProperties notionConfigProps, RestTemplate restTemplate, HttpHeaderUtil httpHeaderUtil) {
        this.notionConfigProps = notionConfigProps;
        this.restTemplate = restTemplate;
        this.httpHeaderUtil = httpHeaderUtil;
    }

    public void updateBlock(String blockId, String requestBody) {
        String url = notionConfigProps.apiUrl() + "/v1/blocks/" + blockId;
        log.info("更新Notion子项数据：{}", url);
        ResponseEntity<String> db = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                new HttpEntity<>(requestBody, httpHeaderUtil.getDefaultHeaders()),
                String.class
        );
        if (db.getStatusCode() != HttpStatus.OK) {
            log.error("更新Notion子项数据失败：{}", db);
        }
    }

}
