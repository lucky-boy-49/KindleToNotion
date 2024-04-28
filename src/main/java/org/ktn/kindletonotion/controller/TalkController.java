package org.ktn.kindletonotion.controller;

import org.ktn.kindletonotion.model.Talk;
import org.ktn.kindletonotion.notion.NotionClient;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 贺佳
 */
@RestController
@RequestMapping("talks")
public class TalkController {

    private final NotionClient client;

    private final NotionConfigProperties notionConfigProperties;

    public TalkController(NotionClient client, NotionConfigProperties notionConfigProperties) {
        this.client = client;
        this.notionConfigProperties = notionConfigProperties;
    }

    /**
     * 查询所有的数据
     * @return 数据
     */
    @GetMapping("/findAll")
    public List<Talk> findAll() {
//        NotionReact<List<PageData>> queryPagesRes = client.database.queryPages(notionConfigProperties.databaseId());
//        if (queryPagesRes.code() == HttpStatus.OK.value()) {
//            return queryPagesRes.data().stream().map(TalksService::mapPageToTalk).toList();
//        } else {
//            return null;
//        }
        return null;
    }

}
