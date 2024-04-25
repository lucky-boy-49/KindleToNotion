package org.ktn.kindletonotion.controller;

import org.ktn.kindletonotion.kindle.KindleClient;
import org.ktn.kindletonotion.kindle.model.Book;
import org.ktn.kindletonotion.model.React;
import org.ktn.kindletonotion.notion.NotionClient;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.model.page.PageData;
import org.ktn.kindletonotion.service.KindleToNotionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Kindle上传Notion控制器
 * @author 贺佳
 */
@RestController
@RequestMapping("kindleToNotion")
public class KindleToNotionController {

    private final NotionClient notionClient;

    private final KindleClient kindleClient;

    private final NotionConfigProperties notionConfigProperties;

    private final KindleToNotionService kindleToNotionService;

    public KindleToNotionController(NotionClient notionClient, KindleClient kindleClient, NotionConfigProperties notionConfigProperties, KindleToNotionService kindleToNotionService) {
        this.notionClient = notionClient;
        this.kindleClient = kindleClient;
        this.notionConfigProperties = notionConfigProperties;
        this.kindleToNotionService = kindleToNotionService;
    }


    @GetMapping("/conversions")
    public React conversions() {

        // 读取所有笔记文件
        String filePath = kindleClient.kindle.getFilePath();
        if (filePath.isEmpty()) {
            return new React("1111", "无法找到文件，请手动选择！！！！", null);
        }
        // 处理文件，并获取文件Map对象
        Map<String, Book> books = kindleClient.kindle.parseNotes(filePath);

        // 查询所有读书笔记下面的所有书信息
        List<PageData> pageDataList = notionClient.database.queryPages(notionConfigProperties.databaseId());
        // 把书信息转换为Map，Key-书名+作者，value-书信息
        Map<String, PageData> pageMap = kindleToNotionService.pagesToMap(pageDataList);

        // 处理书籍进行上传
        books.forEach((bookName, book) -> kindleToNotionService.uploadBookNote(bookName, book, pageMap, notionConfigProperties.databaseId()));

        return new React("00000", "上传成功", null);
    }

}
