package org.ktn.kindletonotion.config;

import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 配置
 * @author 贺佳
 */
@Configuration
public class KindToNotionConfig {

    private final NotionConfigProperties notionConfigProps;

    public KindToNotionConfig(NotionConfigProperties notionConfigProps) {
        this.notionConfigProps = notionConfigProps;
    }

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl(notionConfigProps.apiUrl()).build();
    }

}
