package org.ktn.kindletonotion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ktn.kindletonotion.kindle.model.Book;
import org.ktn.kindletonotion.kindle.model.Mark;
import org.ktn.kindletonotion.model.NotionReact;
import org.ktn.kindletonotion.model.ReactEnum;
import org.ktn.kindletonotion.model.notion.block.type.*;
import org.ktn.kindletonotion.model.notion.page.Page;
import org.ktn.kindletonotion.model.notion.page.Properties;
import org.ktn.kindletonotion.model.result.Result;
import org.ktn.kindletonotion.model.result.Same;
import org.ktn.kindletonotion.model.result.Success;
import org.ktn.kindletonotion.notion.NotionClient;
import org.ktn.kindletonotion.notion.model.Block;
import org.ktn.kindletonotion.notion.model.page.PageContent;
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
    public NotionReact<String> uploadBookNote(String bookName, Book book, Map<String, PageData> pageMap, String databaseId) {

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
                return  new NotionReact<>(ReactEnum.NUMBER_NOTES_IS_SAME.getCode(),
                        book.getName() + "_" + book.getAuthor() + ReactEnum.NUMBER_NOTES_IS_SAME.getMessage(), null);
            }
            // 计算notion的页大小，笔记数*4
            int notionPageSize = markNum * 4;
            if (notionPageSize > 100) {
                // 分段进行更新
                return segmentedUpdate(book, pageId, pageData);
            } else {
                // 一次性更新
                return oneUpdate(book, pageId, notionPageSize, pageData);
            }

        } else {
            // notion中不存在当前图书
            // 1.创建页面
            NotionReact<String> createdPageRes = createPage(book, databaseId);
            if (createdPageRes.code() != HttpStatus.OK.value()) {
                return  new  NotionReact<>(ReactEnum.FAILURE.getCode(), createdPageRes.message(), createdPageRes.data());
            }
            // 获取页面id
            String pageId = createdPageRes.data();
            Objects.requireNonNull(pageId);
            // 2.上传笔记
            // 最后标记时间
            LocalDateTime lastMarkTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
            if (StringUtils.hasLength(pageId)) {
                for (Mark mark : book.getMarks()) {
                    lastMarkTime = lastMarkTime.isBefore(mark.getTime()) ?  mark.getTime() : lastMarkTime;
                    NotionReact<String> appendBlockRes = appendBlock(pageId, mark);
                    if (appendBlockRes.code() != HttpStatus.OK.value()) {
                        return  appendBlockRes;
                    }
                }

            }
            // 更新页面属性
            PageProperties properties = new PageProperties(book.getNums(), lastMarkTime.toString());
            String requestBody = JsonUtil.toJson(properties);
            // 发送更新请求
            NotionReact<String> updatePagePropertiesRes = notionClient.page.updatePageProperties(pageId, requestBody);
            return getNotionReact(book, updatePagePropertiesRes);
        }

    }

    /**
     * 分段更新
     * @param book 书信息
     * @param pageId 页面id
     * @param pageData 页面数据
     * @return 更新结果
     */
    private NotionReact<String> segmentedUpdate(Book book, String pageId, PageData pageData) {
        // 最后标记时间
        LocalDateTime lastMarkTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        int maxSize = 23;
        int i = 0;
        int nums = book.getNums();
        List<Mark> marks = book.getMarks();
        String nextCursor = "";
        boolean hasMore = true;
        // 遍历所有标注进行上传
        label:
        while (hasMore) {
            int j = 0;
            // 获取23*4个Block
            NotionReact<Object> queriedBlocksRes;
            if (nextCursor.isEmpty()) {
                queriedBlocksRes = notionClient.block.queryBlocks(pageId, maxSize * 4);
            } else {
                 queriedBlocksRes = notionClient.block.queryBlocksPagination(pageId, maxSize * 4, nextCursor);
            }
            if (queriedBlocksRes.code() != HttpStatus.OK.value()) {
                 return new NotionReact<>(ReactEnum.FAILURE.getCode(), queriedBlocksRes.message(), null);
            }
            // 获取页的所有子项
            List<Block> blocks = new LinkedList<>();
            if (queriedBlocksRes.data() instanceof PageContent content) {
                hasMore = content.getHasMore();
                nextCursor = content.getNextCursor();
                getBlocks(content, blocks);
            }

            int k = i + maxSize;
            for (; i < Math.min(k, nums); i++) {

                if (j == blocks.size()) {
                    break label;
                }

                // 获取标注
                Mark mark = marks.get(i);
                lastMarkTime = lastMarkTime.isBefore(mark.getTime()) ?  mark.getTime() : lastMarkTime;
                // 更新笔记
                NotionReact<String> updatedBlockRes = uploadBlock(mark,  blocks, j);
                if (updatedBlockRes.code() != HttpStatus.OK.value()) {
                    return updatedBlockRes;
                }
                j += 4;
            }
            // 已经全部更新完成，把多余的block删除
            if (i == nums && j != blocks.size()) {
                for (; j < blocks.size(); j++) {
                    String blockId = blocks.get(j).getId();
                    NotionReact<String> deleteBlockRes = notionClient.block.deleteBlock(blockId);
                    if (deleteBlockRes.code() != HttpStatus.OK.value()) {
                        return deleteBlockRes;
                    }
                }
                break;
            }
        }
        // 如果笔记多余Block则进行追加
        if (i < nums) {
            for (; i < nums; i++) {
                // 获取标注
                Mark mark = marks.get(i);
                lastMarkTime = lastMarkTime.isBefore(mark.getTime()) ?  mark.getTime() : lastMarkTime;
                NotionReact<String> appendBlockRes = appendBlock(pageData.getId(), mark);
                if (appendBlockRes.code() != HttpStatus.OK.value()) {
                    return appendBlockRes;
                }
            }
        }
        // 如果笔记上传完成还有Block则进行删除
        if (hasMore) {
            while (hasMore) {
                // 获取页的所有子项
                NotionReact<Object> queriedBlocks = notionClient.block.
                        queryBlocksPagination(pageId, 100, nextCursor);
                // 获取页的所有子项
                List<Block> blocks = new LinkedList<>();
                if (queriedBlocks.data() instanceof PageContent content) {
                    hasMore = content.getHasMore();
                    nextCursor = content.getNextCursor();
                    getBlocks(content, blocks);
                } else {
                    break;
                }
                for (Block block : blocks) {
                    String blockId = block.getId();
                    NotionReact<String> deleteBlockRes = notionClient.block.deleteBlock(blockId);
                    if (deleteBlockRes.code() != HttpStatus.OK.value()) {
                        return deleteBlockRes;
                    }
                }
            }
        }
        // 更新页面属性
        NotionReact<String> updatePagePropertiesRes = updatePageProperties(book, pageData, lastMarkTime);
        if (updatePagePropertiesRes.code() != HttpStatus.OK.value()) {
            return updatePagePropertiesRes;
        }

        return new  NotionReact<>(ReactEnum.SUCCESS.getCode(), book.getName() + "_" + book.getAuthor() + "上传成功", null);
    }

    /**
     * 获取页面Block数据
     * @param content 页面内容
     * @param blocks 页面块数据
     */
    private static void getBlocks(PageContent content, List<Block> blocks) {
        Object data = content.getBlockList();
        if (data instanceof List<?> blockList) {
            for (Object block : blockList) {
                if (block instanceof Block) {
                    blocks.add((Block) block);
                }
            }
        }
    }

    /**
     * 更新积木
     * @param mark 笔记
     * @param blocks 页面块数据
     * @param j 当前notion Block的下标
     * @return 更新结果
     */
    private NotionReact<String> uploadBlock(Mark mark, List<Block> blocks, int j) {
        // 获取积木id
        String blockId = blocks.get(j++).getId();
        // 更新callout请求体
        Callout callout = new Callout(mark.getContent());
        // 更新callout请求体
        NotionReact<String> updatedBlockRes = toUpload(callout, blockId);
        if (updatedBlockRes.code() != HttpStatus.OK.value()) {
            return updatedBlockRes;
        }

        // 获取积木id
        blockId = blocks.get(j++).getId();
        // 更新quote请求体
        Quote quote = new Quote(mark.getHaveNote() ? mark.getNote() : "无");
        // 更新quote请求体
        updatedBlockRes = toUpload(quote, blockId);
        if (updatedBlockRes.code() != HttpStatus.OK.value()) {
            return updatedBlockRes;
        }

        // 获取积木id
        blockId = blocks.get(j++).getId();
        // 更新paragraph请求体
        Paragraph paragraph = new Paragraph(mark.getAddress());
        // 更新paragraph请求体
        updatedBlockRes = toUpload(paragraph, blockId);
        if (updatedBlockRes.code() != HttpStatus.OK.value()) {
            return updatedBlockRes;
        }

        // 获取积木id
        blockId = blocks.get(j).getId();
        // 更新divider请求体
        Divider divider = new Divider();
        // 更新divider请求体
        updatedBlockRes = toUpload(divider, blockId);
        if (updatedBlockRes.code() != HttpStatus.OK.value()) {
            return updatedBlockRes;
        }
        return new  NotionReact<>(HttpStatus.OK.value(), "更新成功", null);
    }

    /**
     * 更新页面属性
     * @param book 书信息
     * @param pageData 页面数据
     * @param lastMarkTime 最后标记时间
     * @return 更新结果
     */
    private NotionReact<String> updatePageProperties(Book book, PageData pageData, LocalDateTime lastMarkTime) {
        // 更新页面属性
        PageProperties properties = new PageProperties(book.getNums(), lastMarkTime.toString());
        String requestBody = JsonUtil.toJson(properties);
        // 发送更新请求
        return notionClient.page.updatePageProperties(pageData.getId(), requestBody);
    }

    /**
     * 一次性更新
     * @param book 书信息
     * @param pageId 页面id
     * @param notionPageSize 页面大小
     * @param pageData 页面数据
     * @return 更新结果
     */
    private NotionReact<String> oneUpdate(Book book, String pageId, int notionPageSize, PageData pageData) {
        // 获取页的所有子项
        NotionReact<Object> queriedBlocksRes = notionClient.block.queryBlocks(pageId, notionPageSize);
        if (queriedBlocksRes.code() != HttpStatus.OK.value()) {
            return new NotionReact<>(ReactEnum.FAILURE.getCode(), queriedBlocksRes.message(), null);
        }
        // 获取页的所有子项
        List<Block> blocks = new LinkedList<>();
        if (queriedBlocksRes.data() instanceof PageContent content) {
            getBlocks(content, blocks);
        }

        // 上传
        NotionReact<String> upload = upload(book, blocks, notionPageSize, pageData, book.getNums());
        return getNotionReact(book, upload);
    }

    /**
     * 返回书的上传结果
     * @param book 书信息
     * @param updatePagePropertiesRes 更新页面属性结果
     * @return 上传结果
     */
    private NotionReact<String> getNotionReact(Book book, NotionReact<String> updatePagePropertiesRes) {
        if (updatePagePropertiesRes.code() != HttpStatus.OK.value()) {
            return  new  NotionReact<>(ReactEnum.FAILURE.getCode(), updatePagePropertiesRes.message(),
                    updatePagePropertiesRes.data());
        } else {
            return new NotionReact<>(ReactEnum.SUCCESS.getCode(),
                    book.getName() + "_" + book.getAuthor() + ReactEnum.SUCCESS.getMessage(), null);
        }
    }

    /**
     * 创建页面
     * @param book 书信息
     * @param databaseId 数据库id
     * @return 页面id
     */
    private NotionReact<String> createPage(Book book, String databaseId) {
        // 创建页面json
        Page page = new Page(databaseId);
        // 创建页面属性
        Properties properties = NotionUtil.getProperties(book);
        page.setProperties(properties);
        // 创建页面请求体
        String requestBody = JsonUtil.toJson(page);
        // 发送创建页面请求
        NotionReact<Object> createdPageRes = notionClient.page.createPage(requestBody);
        if (createdPageRes.code() != HttpStatus.OK.value()) {
            return new  NotionReact<>(createdPageRes.code(), createdPageRes.message(), null);
        }
        // 获取响应体
        if (createdPageRes.data()  instanceof ResponseEntity<?> response) {
            if (response.getBody() instanceof PageData pageData) {
                // 返回页面id
                return new  NotionReact<>(HttpStatus.OK.value(), book.getName() + "_" + book.getAuthor() + "创建成功",
                        pageData.getId());
            }
        }
        // 创建失败
        return new  NotionReact<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "创建Notion页面返回失败", null);
    }

    /**
     * 上传
     * @param book 书信息
     * @param blocks 页面块数据
     * @param notionPageSize 页面大小
     * @param pageData 页面数据
     * @param totalNum 总笔记数
     * @return 上传结果
     */
    private NotionReact<String> upload(Book book, List<Block> blocks, int notionPageSize, PageData pageData, int totalNum) {
        // 最后标记时间
        LocalDateTime lastMarkTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);

        // 获取所有标注
        List<Mark> marks = book.getMarks();
        // j:当前notion子项的下标
        int j = 0, i = 1;
        // 遍历所有标注进行上传
        for (; i < totalNum + 1; i++) {
            // 获取标注
            Mark mark = marks.get(i - 1);
            lastMarkTime = lastMarkTime.isBefore(mark.getTime()) ?  mark.getTime() : lastMarkTime;

            // 当前的标注未超出notion页已有的笔记数，即（i-1）*4<=notionPageSize
            if ((i - 1) * 4 <= notionPageSize) {
                // 更新笔记
                NotionReact<String> updatedBlockRes = uploadBlock(mark, blocks, j);
                if (updatedBlockRes.code() != HttpStatus.OK.value()) {
                    return updatedBlockRes;
                }
                j += 4;
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
        NotionReact<String> updatePagePropertiesRes = updatePageProperties(book, pageData, lastMarkTime);
        if (updatePagePropertiesRes.code() != HttpStatus.OK.value()) {
            return updatePagePropertiesRes;
        }

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
        return new  NotionReact<>(HttpStatus.OK.value(), "更新成功", null);
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

    public Result toResult (List<String> successList, List<String> sameList) {
        Result result = new Result();
        List<Success> success = new LinkedList<>();
        for (String s : successList) {
            success.add(new Success(s, "上传成功"));
        }
        result.setSuccess(success);
        List<Same> same = new LinkedList<>();
        for (String s : sameList) {
            same.add(new Same(s, "笔记数一致，不进行更新"));
        }
        result.setSames(same);
        return result;
    }

}
