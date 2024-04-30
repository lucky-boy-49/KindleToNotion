package org.ktn.kindletonotion.notion.service;

import org.ktn.kindletonotion.notion.model.page.PageContent;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PatchExchange;

/**
 * notion Block API
 * @author 贺佳
 */
public interface BlockService {

    /**
     * 更新块数据
     * @param blockId 块Id
     * @param requestBody 块数据
     * @return 更新结果
     */
    @PatchExchange(value = "/v1/blocks/{blockId}", contentType = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> patchBlock(@PathVariable String blockId, @RequestBody String requestBody);

    /**
     * 查询页面下的所有块
     * @param pageId 页面Id
     * @param pageSize 每页大小
     * @return 块数据
     */
    @GetExchange(value = "/v1/blocks/{pageId}/children?page_size={pageSize}")
    ResponseEntity<PageContent> queryBlocks(@PathVariable String pageId, @PathVariable int pageSize);

    /**
     * 向页面追加块
     * @param pageId 页面Id
     * @param requestBody 块数据
     * @return 追加结果
     */
    @PatchExchange(value = "/v1/blocks/{pageId}/children", contentType = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> additionBlock(@PathVariable String pageId, @RequestBody String requestBody);

    /**
     * 删除块
     * @param blockId 块Id
     * @return 删除结果
     */
    @DeleteExchange(value = "/v1/blocks/{blockId}")
    ResponseEntity<String> deleteBlock(@PathVariable String blockId);

    /**
     * 分页查询块
     * @param pageId 页面Id
     * @param pageSize 每页大小
     * @param nextCursor 下一页游标
     * @return 块数据
     */
    @GetExchange(value = "/v1/blocks/{pageId}/children?page_size={pageSize}&start_cursor={nextCursor}")
    ResponseEntity<PageContent> queryBlocksPagination(@PathVariable String pageId, @PathVariable int pageSize, @PathVariable String nextCursor);
}
