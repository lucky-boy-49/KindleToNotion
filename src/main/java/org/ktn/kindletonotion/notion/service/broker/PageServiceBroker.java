package org.ktn.kindletonotion.notion.service.broker;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.Exception.NotionResponseException;
import org.ktn.kindletonotion.model.NotionReact;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.model.page.PageData;
import org.ktn.kindletonotion.notion.service.PageService;
import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * 页面操作
 * @author 贺佳
 */
@Slf4j
@Service
public class PageServiceBroker {

    private final NotionConfigProperties notionConfigProps;

    private final HttpServiceProxyFactory factory;

    private final HttpHeaderUtil httpHeaderUtil;

    public PageServiceBroker(NotionConfigProperties notionConfigProps, HttpServiceProxyFactory factory, HttpHeaderUtil httpHeaderUtil) {
        this.notionConfigProps = notionConfigProps;
        this.factory = factory;
        this.httpHeaderUtil = httpHeaderUtil;
    }


    /**
     * 更新页属性
     * @param pageId 页id
     * @param requestBody 属性值
     */
    public NotionReact<String> updatePageProperties(String pageId, String requestBody) {
        try {
            log.info("更新Notion页面属性");
            PageService service = factory.createClient(PageService.class);
            ResponseEntity<PageData> response = service.updatePageProperties(pageId,requestBody);
            log.info("更新Notion页面属性返回成功");
            return new NotionReact<>(response.getStatusCode().value(), "更新页面属性成功", null);
        } catch (NotionResponseException e) {
            log.error("更新Notion页面属性返回失败，错误码：{}，错误信息：{}", e.getCode(), e.getMessage());
            return new NotionReact<>(e.getCode(), "更新Notion页面属性返回失败", e.getMessage());
        } catch (Exception e) {
            log.error("更新Notion页面属性返回失败，错误信息：{}", e.getMessage());
            return new NotionReact<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "更新Notion页面属性返回失败", e.getMessage());
        }
    }

    public ResponseEntity<PageData> createPage(String requestBody) {
        log.info("创建Notion页面");
        WebClient client = WebClient.builder().baseUrl(notionConfigProps.apiUrl()).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        PageService service = factory.createClient(PageService.class);
        ResponseEntity<PageData> response = service.createPage(requestBody,  httpHeaderUtil.getDefaultHeaders());
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("创建页面失败：{}", response);
        }
        return response;
    }

}
