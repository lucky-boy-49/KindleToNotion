package org.ktn.kindletonotion.notion.config;

import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * notion配置
 * @author 贺佳
 */
@Configuration
public class NotionConfig {

    /**
     * notion配置信息
     */
    private final NotionConfigProperties notionConfigProps;

    public NotionConfig(NotionConfigProperties notionConfigProps) {
        this.notionConfigProps = notionConfigProps;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 请求头bean
     * @return 请求头
     */
    @Bean
    public HttpHeaderUtil httpHeaderUtil() {
        return new HttpHeaderUtil(notionConfigProps);
    }

}
