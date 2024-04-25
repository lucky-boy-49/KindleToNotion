package org.ktn.kindletonotion.utils;

import org.ktn.kindletonotion.notion.model.page.PageData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 把从notion查出来的页信息转化为Map。
 * @author 贺佳
 */
public class PageListToMapUtil {

    public static Map<String, PageData> toPgeMap(List<PageData> pageDataList) {

        Map<String, PageData> pageMap = new HashMap<>(16);

        // 循环处理页面列表，生成以书名加作者未key，页信息为value的map
        pageDataList.forEach(page -> {
            String author = page.getProperties().get("作者").get("rich_text").get(0).get("text").get("content").asText();
            String title = page.getProperties().get("Title").get("title").get(0).get("text").get("content").asText();
            String key = title + "_" + author;
            pageMap.computeIfAbsent(key, k -> page);
        });

        return pageMap;
    }

}
