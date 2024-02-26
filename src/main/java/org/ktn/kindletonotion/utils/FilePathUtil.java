package org.ktn.kindletonotion.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 获取可用的文件路径工具
 * @author 贺佳
 */
@Slf4j
public class FilePathUtil {

    /**
     * 获取标注文件所在的地址
     * @return 文件地址
     */
    public static String getFilePath() {
        String filePath = "";
        // 获取所有的盘符.遍历盘符，判断需要的路径是否存在
        for (File file : File.listRoots()) {
            String path = file.getPath();
            String name = getDiskName(path);
            path = file.getAbsolutePath() + "documents\\My Clippings.txt";
            if (new File(path).exists() && "Kindle".equals(name)) {
                filePath = path;
            }
        }
        return filePath;
    }

    /**
     * 获取磁盘名称
     * @param path 磁盘路径
     * @return 磁盘名称
     */
    private static String getDiskName(String path) {
        String name = "";
        Path p = FileSystems.getDefault().getPath(path);
        try {
            FileStore store = Files.getFileStore(p);
            name = store.name();
        } catch (IOException e) {
            log.error("读取磁盘的名称错误：", e);
        }
        return name;
    }

}
