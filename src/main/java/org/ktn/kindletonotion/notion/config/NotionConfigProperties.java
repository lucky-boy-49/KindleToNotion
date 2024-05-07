package org.ktn.kindletonotion.notion.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.model.result.Deploy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationHome;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * notion配置类
 * @author 贺佳
 */
@Data
@Slf4j
@ConfigurationProperties("notion")
public class NotionConfigProperties {
    private String apiUrl;
    private String apiVersion;
    private String authToken;
    private String databaseId;

    public void save(Deploy deploy) {
        try {
            ApplicationHome h = new ApplicationHome(getClass());
            String path = h.getSource().getParentFile().toString();
            log.info("JAR包所在目录:{}", path);
            String filePath = path + File.separator + "application.properties";
            log.info("配置文件路径:{}", filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                boolean success = file.createNewFile();
                if (success) {
                    log.info("创建配置文件成功,目录:{}", filePath);
                } else {
                    log.error("创建配置文件失败");
                }
            }
            OutputStream outputStream = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            writer.write("");
            writer.write("# notion配置" + "\n");
            writer.write("notion.auth-token=" + deploy.authToken() + "\n");
            writer.write("notion.database-id=" + deploy.databaseId() + "\n");
            writer.close();

        } catch (Exception e) {
            log.error("写入配置文件错误：", e);
            throw new RuntimeException(e);
        }
    }
}
