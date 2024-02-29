package org.ktn.kindletonotion.notion.service;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.model.Block;
import org.ktn.kindletonotion.notion.model.PageContent;
import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PageService {

    private final NotionConfigProperties notionConfigProps;

    private final RestTemplate restTemplate;

    private final HttpHeaderUtil httpHeaderUtil;

    public PageService(NotionConfigProperties notionConfigProps, RestTemplate restTemplate, HttpHeaderUtil httpHeaderUtil) {
        this.notionConfigProps = notionConfigProps;
        this.restTemplate = restTemplate;
        this.httpHeaderUtil = httpHeaderUtil;
    }


    /**
     * 获取一个页面下的所有子项
     * @param pageId 页id
     * @param pageSize 页大小
     * @return 子项数据
     */
    public List<Block> queryBlocks(String pageId, int pageSize) {
        String url = notionConfigProps.apiUrl() + "/v1/blocks/" + pageId + "children?page_size=" + pageSize;
        log.info("查询Notion页数据：{}", url);
        ResponseEntity<PageContent> db = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaderUtil.getDefaultHeaders()),
                PageContent.class
        );

        return Objects.requireNonNull(db.getBody()).getBlockList();
    }

}
