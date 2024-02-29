package org.ktn.kindletonotion.service;

import org.ktn.kindletonotion.kindle.model.Book;
import org.ktn.kindletonotion.kindle.model.Mark;
import org.ktn.kindletonotion.model.Callout;
import org.ktn.kindletonotion.notion.NotionClient;
import org.ktn.kindletonotion.notion.model.Block;
import org.ktn.kindletonotion.notion.model.PageData;
import org.ktn.kindletonotion.utils.PageListToMapUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public void uploadBookNote(String bookName, Book book, Map<String, PageData> pageMap) {

        if (pageMap.containsKey(bookName)) {
            // notion中存在
            // 获取页面数据
            PageData pageData = pageMap.get(bookName);
            // 获取页的id
            String pageId = pageData.getId();
            // 计算notion的页大小，笔记数*4
            int notionPageSize = pageData.getProperties().get("笔记数").get("number").asInt() * 4;
            // 获取页的详细数据
            List<Block> blocks = notionClient.page.queryBlocks(pageId, notionPageSize);
            upload(book, blocks, notionPageSize);

        } else {
            // notion中不存在

        }

    }

    private void upload(Book book, List<Block> blocks, int notionPageSize) {
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
            // 当前的标注未超出notion页已有的笔记数，即（i-1）*4<=notionPageSize
            if ((i - 1) * 4 <= notionPageSize) {
                // 更新标注
                // 获取积木id
                String blockId = blocks.get(j++).getId();
                // 更新标注请求体
                Callout callout = new Callout();
                callout.getRich_text()[0].getText().setContent(mark.getContent());
                String requestBody  = callout.toString();
                // 发送更新请求
                notionClient.block.updateBlock(blockId, requestBody);

            }
        }
    }


}
