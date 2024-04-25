package org.ktn.kindletonotion.notion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * notion配置记录类
 * @param apiUrl api网址
 * @param apiVersion api版本
 * @param authToken 认证token
 * @param databaseId 数据库Id
 * @author 贺佳
 */
@ConfigurationProperties("notion")
public record NotionConfigProperties(String apiUrl, String apiVersion, String authToken, String databaseId) {

}
