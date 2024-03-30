package org.ktn.kindletonotion.notion.service.broker;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.model.Block;
import org.ktn.kindletonotion.notion.model.PageContent;
import org.ktn.kindletonotion.notion.service.BlockService;
import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Objects;

/**
 * Block操作
 * @author 贺佳
 */
@Slf4j
@Service
public class BlockServiceBroker {

    private final NotionConfigProperties notionConfigProps;


    private final HttpHeaderUtil httpHeaderUtil;

    public BlockServiceBroker(NotionConfigProperties notionConfigProps, HttpHeaderUtil httpHeaderUtil) {
        this.notionConfigProps = notionConfigProps;
        this.httpHeaderUtil = httpHeaderUtil;
    }

    /**
     * 使用WebClient调用Notion API 更新Block
     * @param blockId 子项ID
     * @param requestBody 请求体
     */
    public void updateBlock(String blockId, String requestBody) {
        log.info("更新Notion页数据");
        WebClient client = WebClient.builder().baseUrl(notionConfigProps.apiUrl()).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        BlockService service = factory.createClient(BlockService.class);
        ResponseEntity<String> response = service.patchBlock(blockId, requestBody, httpHeaderUtil.getDefaultHeaders());
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("更新Notion子项数据失败：{}", response);
        }
    }

    /**
     * 获取某个页面下的所有子项
     * @param pageId 页id
     * @param pageSize 页大小
     * @return 子项数据
     */
    public List<Block> queryBlocks(String pageId, int pageSize) {
        log.info("查询Notion页数据");
        WebClient client = WebClient.builder().baseUrl(notionConfigProps.apiUrl()).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        BlockService service = factory.createClient(BlockService.class);
        ResponseEntity<PageContent> response = service.queryBlocks(pageId, pageSize, httpHeaderUtil.getDefaultHeaders());
        return Objects.requireNonNull(response.getBody()).getBlockList();
    }

}
