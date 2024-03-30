package org.ktn.kindletonotion.notion.service;

import org.ktn.kindletonotion.notion.model.PageContent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PatchExchange;

/**
 * notion Block API
 * @author 贺佳
 */
public interface BlockService {

    @PatchExchange(value = "/v1/blocks/{blockId}", contentType = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> patchBlock(@PathVariable String blockId, @RequestBody String requestBody, @RequestHeader HttpHeaders headers);

    @GetExchange(value = "/v1/blocks/{pageId}/children?page_size={pageSize}")
    ResponseEntity<PageContent> queryBlocks(String pageId, int pageSize, @RequestHeader HttpHeaders headers);

}
