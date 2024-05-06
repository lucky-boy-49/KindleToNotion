package org.ktn.kindletonotion.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.kindle.KindleClient;
import org.ktn.kindletonotion.kindle.model.Book;
import org.ktn.kindletonotion.model.NotionReact;
import org.ktn.kindletonotion.model.React;
import org.ktn.kindletonotion.model.ReactEnum;
import org.ktn.kindletonotion.model.Schedule;
import org.ktn.kindletonotion.model.result.Deploy;
import org.ktn.kindletonotion.notion.NotionClient;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.model.page.PageData;
import org.ktn.kindletonotion.service.KindleToNotionService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Kindle上传Notion控制器
 * @author 贺佳
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("kindleToNotion")
public class KindleToNotionController {

    private final NotionClient notionClient;

    private final KindleClient kindleClient;

    private final NotionConfigProperties notionConfigProperties;

    private final KindleToNotionService kindleToNotionService;

    private final Schedule schedule;


    /**
     * 上传Kindle笔记到Notion接口
     * @return 上传结果
     */
    @GetMapping("/conversions")
    public React conversions(@RequestParam String databaseId, @RequestParam String authToken) {

        if ((!StringUtils.hasLength(databaseId) || !StringUtils.hasLength(authToken))
                && (!StringUtils.hasLength(notionConfigProperties.getDatabaseId()) ||
                !StringUtils.hasLength(notionConfigProperties.getAuthToken()))) {
            return new React(ReactEnum.FAILURE.getCode(), "第一次请传入配置信息！！！", null);
        }

        // 配置处理
        kindleToNotionService.setDeploy(new Deploy(databaseId, authToken));

        // 读取所有笔记文件
        String filePath = kindleClient.kindle.getFilePath();
        if (filePath.isEmpty()) {
            return new React(ReactEnum.FAILURE.getCode(), "无法找到文件", null);
        }
        // 处理文件，并获取文件Map对象
        Map<String, Book> books = kindleClient.kindle.parseNotes(filePath);

        // 查询所有读书笔记下面的所有书信息
        List<PageData> pageDataList = new ArrayList<>();
        NotionReact<Object> queryPagesRes = notionClient.database.queryPages(notionConfigProperties.getDatabaseId());
        if (HttpStatus.OK.value() == queryPagesRes.code()) {
            Object data = queryPagesRes.data();
            if (data instanceof List<?>) {
                for (Object datum : (List<?>) data) {
                    if (datum instanceof PageData) {
                        pageDataList.add((PageData) datum);
                    } else {
                        log.error("数据库数据转换失败，目标格式：PageData，当前格式不是该格式子类");
                        return new React(HttpStatus.INTERNAL_SERVER_ERROR.value(), "数据库数据转换失败", null);
                    }
                }
            } else {
                log.error("数据库数据转换失败，目标格式：List<PageData>，当前格式：{}", data.getClass().getName());
                return new React(HttpStatus.INTERNAL_SERVER_ERROR.value(), "数据库数据转换失败", null);
            }
        } else {
            return new React(queryPagesRes.code(), queryPagesRes.message(), queryPagesRes.data());
        }

        // 把书信息转换为Map，Key-书名+作者，value-书信息
        Map<String, PageData> pageMap = kindleToNotionService.pagesToMap(pageDataList);

        log.info("共有{}本书需要上传", books.size());
        // 记录结果
        List<String> successList = new ArrayList<>();
        List<String> sameList = new ArrayList<>();

        // 处理书籍进行上传
        double i = 0;
        for (Map.Entry<String, Book> entry : books.entrySet()) {
            String bookName = entry.getKey();
            Book book = entry.getValue();
            NotionReact<String> uploaded = kindleToNotionService.uploadBookNote(bookName, book, pageMap, notionConfigProperties.getDatabaseId());
            if (uploaded.code() == ReactEnum.FAILURE.getCode()) {
                // 上传失败，直接返回
                return new React(uploaded.code(), uploaded.message(), uploaded.data());
            } else if (uploaded.code() == ReactEnum.NUMBER_NOTES_IS_SAME.getCode()) {
                 sameList.add(bookName);
            } else if (uploaded.code() == ReactEnum.SUCCESS.getCode()) {
                 successList.add(bookName);
            }
            // 设置进度
            i++;
            schedule.setNum(i / books.size());
            schedule.setDate(bookName + "上传成功");
        }
        log.info("上传成功的书籍有：{}", successList);
        log.info("笔记数量相同的书籍有：{}", sameList);

        return new React(HttpStatus.OK.value(), "上传成功", kindleToNotionService.toResult(successList, sameList));
    }

    /**
     * 获取定时任务信息
     * @return 定时任务信息
     */
    @GetMapping("/schedule")
    public React schedule() {
        log.info("定时任务：{}", schedule);
        return new React(HttpStatus.OK.value(), "定时任务", schedule);
    }

}
