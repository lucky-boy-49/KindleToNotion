package org.ktn.kindletonotion.notion.service;

import org.ktn.kindletonotion.notion.model.page.PageData;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Notion页面API
 * @author 贺佳
 */
public interface PageService {

    /**
     * 更新页面属性
     * @param pageId 页面ID
     * @param headers 请求头
     * @return 页面数据
     */
    @PatchExchange(value = "/v1/pages/{pageId}", contentType = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageData> updatePageProperties(@PathVariable String pageId, @RequestBody String requestBody);

    /**
     * 创建一个页面
     * @param requestBody 页面属性信息
     * @param headers 请求头
     * @return 页面数据
     */
    @PostExchange(value = "/v1/pages", contentType = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageData> createPage(@RequestBody String requestBody);

}
