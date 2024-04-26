package org.ktn.kindletonotion;

import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * KindleToNotion应用
 * @author 贺佳
 */
@SpringBootApplication
@EnableConfigurationProperties(NotionConfigProperties.class)
public class KindleToNotionApplication {

    public static void main(String[] args) {
        SpringApplication.run(KindleToNotionApplication.class, args);
    }

}
