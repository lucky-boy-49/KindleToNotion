package org.ktn.kindletonotion.notion.service.broker;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.Exception.NotionResponseException;
import org.ktn.kindletonotion.model.NotionReact;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.model.page.PageContent;
import org.ktn.kindletonotion.notion.service.BlockService;
import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

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

    private final WebClient client;

    public BlockServiceBroker(NotionConfigProperties notionConfigProps, HttpHeaderUtil httpHeaderUtil, WebClient client) {
        this.notionConfigProps = notionConfigProps;
        this.httpHeaderUtil = httpHeaderUtil;
        this.client = client;
    }

    /**
     * 使用WebClient调用Notion API 更新Block
     * @param blockId 子项ID
     * @param requestBody 请求体
     */
    public NotionReact<String> updateBlock(String blockId, String requestBody) {
        try {
            log.info("更新Notion页数据");
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
            BlockService service = factory.createClient(BlockService.class);
            ResponseEntity<String> response = service.patchBlock(blockId, requestBody);
            log.info("更新Notion页数据成功");
            return new NotionReact<>(response.getStatusCode().value(), "更新Notion页数据成功", null);
        }catch (NotionResponseException e) {
            log.error("更新Notion页数据失败，错误码：{}，错误信息：{}", e.getCode(), e.getMessage());
            return new NotionReact<>(e.getCode(), "更新Notion页数据失败", e.getMessage());
        } catch (Exception e) {
            log.error("更新Notion页数据失败：{}", e.getMessage());
            return new NotionReact<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "更新Notion页数据失败", e.getMessage());
        }
    }

    /**
     * 获取某个页面下的所有BLock
     * @param pageId 页id
     * @param pageSize 页大小
     * @return 子项数据
     */
    public NotionReact<Object> queryBlocks(String pageId, int pageSize) {
        try {
            log.info("查询Notion页数据");
            WebClient client = WebClient.builder().baseUrl(notionConfigProps.apiUrl()).build();
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
            BlockService service = factory.createClient(BlockService.class);
            ResponseEntity<PageContent> response = service.queryBlocks(pageId, pageSize, httpHeaderUtil.getDefaultHeaders());
            log.info("查询Notion页数据成功");
            return new NotionReact<>(response.getStatusCode().value(), "查询Notion页数据成功",
                    Objects.requireNonNull(response.getBody()).getBlockList());
        } catch (NotionResponseException e) {
            log.error("查询Notion页数据失败，错误码：{}，错误信息：{}", e.getCode(), e.getMessage());
            return new NotionReact<>(e.getCode(), "查询Notion页数据失败", e.getMessage());
        } catch (Exception e) {
            log.error("查询Notion页数据失败：{}", e.getMessage());
            return new NotionReact<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "查询Notion页数据失败", e.getMessage());
        }
    }

    /**
     * 向页面追加Block
     * @param pageId 页id
     * @param requestBody 请求体
     */
    public NotionReact<String> additionBlock(String pageId, String requestBody) {
        try {
            log.info("向页面追加子项");
            WebClient client = WebClient.builder().baseUrl(notionConfigProps.apiUrl()).build();
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
            BlockService service = factory.createClient(BlockService.class);
            ResponseEntity<String> response = service.additionBlock(pageId, requestBody, httpHeaderUtil.getDefaultHeaders());
            log.info("向页面追加子项成功");
            return new NotionReact<>(response.getStatusCode().value(), "向页面追加子项成功", null);
        } catch (NotionResponseException e) {
            log.error("向页面追加子项失败，错误码：{}，错误信息：{}", e.getCode(), e.getMessage());
            return new NotionReact<>(e.getCode(), "向页面追加子项失败", e.getMessage());
        } catch (Exception e) {
            log.error("向页面追加子项失败：{}", e.getMessage());
            return new NotionReact<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "向页面追加子项失败", e.getMessage());
        }
    }

    /**
     * 删除页面中的某个块
     * @param blockId 块Id
     */
    public void deleteBlock(String blockId) {
        log.info("删除子项");
        WebClient client = WebClient.builder().baseUrl(notionConfigProps.apiUrl()).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        BlockService service = factory.createClient(BlockService.class);
        ResponseEntity<String> response = service.deleteBlock(blockId, httpHeaderUtil.getDefaultHeaders());
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("删除子项失败：{}", response);
        }
    }
}
