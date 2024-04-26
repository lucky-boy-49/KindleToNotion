package org.ktn.kindletonotion.notion.service;

import org.ktn.kindletonotion.notion.model.Database;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Notion数据库Api
 * @author 贺佳
 */
public interface DatabaseService {

    @PostExchange(value = "/v1/databases/{databaseId}/query", contentType = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Database> queryPages(@PathVariable String databaseId);

}
