package org.ktn.kindletonotion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ktn.kindletonotion.kindle.model.Book;
import org.ktn.kindletonotion.kindle.model.Mark;
import org.ktn.kindletonotion.model.NotionReact;
import org.ktn.kindletonotion.model.notion.block.type.*;
import org.ktn.kindletonotion.model.notion.page.Page;
import org.ktn.kindletonotion.model.notion.page.Properties;
import org.ktn.kindletonotion.notion.NotionClient;
import org.ktn.kindletonotion.notion.model.Block;
import org.ktn.kindletonotion.notion.model.page.PageData;
import org.ktn.kindletonotion.notion.model.page.PageProperties;
import org.ktn.kindletonotion.notion.utils.JsonUtil;
import org.ktn.kindletonotion.notion.utils.NotionUtil;
import org.ktn.kindletonotion.utils.PageListToMapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * kindle上传notion服务
 * @author 贺佳
 */
@Service
public class KindleToNotionService {

    private final NotionClient notionClient;

    public KindleToNotionService(NotionClient notionClient) {
        this.notionClient = notionClient;
    }

    /**
     * 把数据库页下面的页转化为Map
     * @param pageDataList 页面数据
     * @return 页面数据Map
     */
    public Map<String, PageData> pagesToMap(List<PageData> pageDataList) {
        // 把数据库页下面的页转化为Map
        return PageListToMapUtil.toPgeMap(pageDataList);
    }

    /**
     * 上传图书笔记，分为数据库有该书和没有该书分别进行上传
     * @param bookName 书名
     * @param book 图书
     * @param pageMap 页面数据Map
     * @param databaseId 数据库id
     */
    public void uploadBookNote(String bookName, Book book, Map<String, PageData> pageMap, String databaseId) {

        if (pageMap.containsKey(bookName)) {
            // notion中存在
            // 获取页面数据
            PageData pageData = pageMap.get(bookName);
            // 获取页的id
            String pageId = pageData.getId();
            // 获取notion中笔记个数
            int markNum = pageData.getProperties().get("笔记数").get("number").asInt();
            // 如果kindle中的笔记数量与notion中的笔记数量一致则不进行更新
            if (markNum == book.getNums()) {
                return;
            }
            // 计算notion的页大小，笔记数*4
            int notionPageSize = markNum * 4;
            // 获取页的所有子项
            NotionReact<Object> queriedBlocksRes = notionClient.block.queryBlocks(pageId, notionPageSize);
            if (queriedBlocksRes.code() != HttpStatus.OK.value()) {
                return;
            }
            // 获取页的所有子项
            List<Block> blocks = new LinkedList<>();
            Object data = queriedBlocksRes.data();
            if (data instanceof List<?> blockList) {
                for (Object block : blockList) {
                    if (block instanceof Block) {
                        blocks.add((Block) block);
                    }
                }
            }
            // 上传
            upload(book, blocks, notionPageSize, pageData);

        } else {
            // notion中不存在当前图书
            // 1.创建页面
            String pageId = createPage(book, databaseId);
            // 2.上传笔记
            // 最后标记时间
            LocalDateTime lastMarkTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
            if (StringUtils.hasLength(pageId)) {
                for (Mark mark : book.getMarks()) {
                    lastMarkTime = lastMarkTime.isBefore(mark.getTime()) ?  mark.getTime() : lastMarkTime;
                    NotionReact<String> appendBlockRes = appendBlock(pageId, mark);
                    if (appendBlockRes.code() != HttpStatus.OK.value()) {
                        return;
                    }
                }

            }
            // 更新页面属性
            PageProperties properties = new PageProperties(book.getNums(), lastMarkTime.toString());
            String requestBody = JsonUtil.toJson(properties);
            // 发送更新请求
            notionClient.page.updatePageProperties(pageId, requestBody);
        }

    }

    /**
     * 创建页面
     * @param book 书信息
     * @param databaseId 数据库id
     * @return 页面id
     */
    private String createPage(Book book, String databaseId) {
        // 创建页面json
        Page page = new Page(databaseId);
        // 创建页面属性
        Properties properties = NotionUtil.getProperties(book);
        page.setProperties(properties);
        // 创建页面请求体
        String requestBody = JsonUtil.toJson(page);
        // 发送创建页面请求
        ResponseEntity<PageData> response = notionClient.page.createPage(requestBody);
        // 返回页面id
        return Objects.requireNonNull(response.getBody()).getId();
    }

    /**
     * 上传
     * @param book 书信息
     * @param blocks 页面块数据
     * @param notionPageSize 页面大小
     * @param pageData 页面数据
     */
    private NotionReact<String> upload(Book book, List<Block> blocks, int notionPageSize, PageData pageData) {
        // 最后标记时间
        LocalDateTime lastMarkTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);

        // 标注总数
        Integer totalNum = book.getNums();
        // 获取所有标注
        List<Mark> marks = book.getMarks();
        // i:当前已遍历的标注数,j:当前notion子项的下标
        int i = 1, j = 0;
        // 遍历所有标注进行上传
        for (; i < totalNum + 1; i++) {
            // 获取标注
            Mark mark = marks.get(i - 1);
            lastMarkTime = lastMarkTime.isBefore(mark.getTime()) ?  mark.getTime() : lastMarkTime;

            // 当前的标注未超出notion页已有的笔记数，即（i-1）*4<=notionPageSize
            if ((i - 1) * 4 <= notionPageSize) {
                // 更新笔记

                // 获取积木id
                String blockId = blocks.get(j++).getId();
                // 更新callout请求体
                Callout callout = new Callout(mark.getContent());
                // 更新callout请求体
                NotionReact<String> updatedBlockRes = toUpload(callout, blockId);
                if (updatedBlockRes != null) {
                    return updatedBlockRes;
                }

                // 获取积木id
                blockId = blocks.get(j++).getId();
                // 更新quote请求体
                Quote quote = new Quote(mark.getHaveNote() ? mark.getNote() : "无");
                // 更新quote请求体
                updatedBlockRes = toUpload(quote, blockId);
                if (updatedBlockRes != null) {
                    return updatedBlockRes;
                }

                // 获取积木id
                blockId = blocks.get(j++).getId();
                // 更新paragraph请求体
                Paragraph paragraph = new Paragraph(mark.getAddress());
                // 更新paragraph请求体
                updatedBlockRes = toUpload(paragraph, blockId);
                if (updatedBlockRes != null) {
                    return updatedBlockRes;
                }

                // 获取积木id
                blockId = blocks.get(j++).getId();
                // 更新divider请求体
                Divider divider = new Divider();
                // 更新divider请求体
                updatedBlockRes = toUpload(divider, blockId);
                if (updatedBlockRes != null) {
                    return updatedBlockRes;
                }
                
            } else if ((i - 1) * 4 > notionPageSize) {
                // 如果笔记数大于当前notion笔记则进行追加笔记
                NotionReact<String> appendBlockRes = appendBlock(pageData.getId(), mark);
                if (appendBlockRes.code() != HttpStatus.OK.value()) {
                    return appendBlockRes;
                }
            }
        }
        // 如果当前笔记少于notion中的笔记则删除多余的子项
        if (i * 4 < notionPageSize) {
            for (; j < notionPageSize; j++) {
                String blockId = blocks.get(j).getId();
                NotionReact<String> deleteBlockRes = notionClient.block.deleteBlock(blockId);
                if (deleteBlockRes.code() != HttpStatus.OK.value()) {
                    return deleteBlockRes;
                }
            }
        }
        // 更新页面属性
        PageProperties properties = new PageProperties(book.getNums(), lastMarkTime.toString());
        String requestBody = JsonUtil.toJson(properties);
        // 发送更新请求
        notionClient.page.updatePageProperties(pageData.getId(), requestBody);

        return new  NotionReact<>(HttpStatus.OK.value(), book.getName() + "_" + book.getAuthor() + "上传成功", null);
    }

    /**
     * 执行block更新请求
     * @param object 请求体
     * @param blockId BlockId
     * @return 更新结果
     */
    private NotionReact<String> toUpload(Object object, String blockId) {
        String requestBody = JsonUtil.toJson(object);
        // 发送更新请求
        NotionReact<String> updatedBlockRes = notionClient.block.updateBlock(blockId, requestBody);
        if (updatedBlockRes.code() != HttpStatus.OK.value()) {
            return new NotionReact<>(updatedBlockRes.code(), updatedBlockRes.message(), updatedBlockRes.data());
        }
        return null;
    }

    /**
     * 追加笔记
     * @param pageId 页面id
     * @param mark 笔记
     */
    private NotionReact<String> appendBlock(String pageId, Mark mark) {
        Children children = getChildren(mark);
        // 创建请求体
        String requestBody = JsonUtil.toJson(children);
        // 发送追加请求
        return notionClient.block.additionBlock(pageId, requestBody);
    }

    /**
     * 把笔记转换为json
     * @param mark 笔记
     * @return 笔记json
     */
    private static Children getChildren(Mark mark) {
        ObjectMapper mapper = new ObjectMapper();
        Children children = new Children();

        // 创建callout
        Callout callout = new Callout(mark.getContent(), "gray_background");
        children.setCallout(mapper.valueToTree(callout));
        // 创建quote
        Quote quote = new Quote(mark.getHaveNote() ? mark.getNote() : "无");
        children.setQuote(mapper.valueToTree(quote));
        // 创建paragraph
        Paragraph paragraph = new Paragraph(mark.getAddress(), "");
        children.setParagraph(mapper.valueToTree(paragraph));
        // 创建divider
        Divider divider = new Divider();
        children.setDivider(mapper.valueToTree(divider));
        return children;
    }


}
