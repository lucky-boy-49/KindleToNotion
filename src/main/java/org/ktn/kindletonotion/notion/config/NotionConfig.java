package org.ktn.kindletonotion.notion.config;

import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author 贺佳
 */
@Configuration
public class NotionConfig {

    private final NotionConfigProperties notionConfigProps;

    public NotionConfig(NotionConfigProperties notionConfigProps) {
        this.notionConfigProps = notionConfigProps;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpHeaderUtil httpHeaderUtil() {
        return new HttpHeaderUtil(notionConfigProps);
    }

}
