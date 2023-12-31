package org.ktn.kindletonotion.notion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 贺佳
 * notion配置记录类
 */
@ConfigurationProperties("notion")
public record NotionConfigProperties(String apiUrl, String apiVersion, String authToken, String databaseId) {

}
