package org.ktn.kindletonotion.notion.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.model.result.Deploy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.io.FileWriter;

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
            // 获取JAR包的路径
            String jarPath = NotionConfigProperties.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            // 获取JAR包所在的目录路径
            String jarDirectory = new File(jarPath).getParent();
            String filePath = jarDirectory + File.separator + "secrets.properties";
            File file = new File(filePath);
            if (!file.exists()) {
                boolean success = file.createNewFile();
                if (success) {
                    log.info("创建配置文件成功,目录:{}", filePath);
                } else {
                    log.error("创建配置文件失败");
                }
            }
            FileWriter writer = new FileWriter(file);
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
