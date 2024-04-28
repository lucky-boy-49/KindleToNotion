package org.ktn.kindletonotion.notion.service.broker;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.Exception.NotionResponseException;
import org.ktn.kindletonotion.model.NotionReact;
import org.ktn.kindletonotion.notion.model.page.PageData;
import org.ktn.kindletonotion.notion.service.PageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * 页面操作
 * @author 贺佳
 */
@Slf4j
@Service
public class PageServiceBroker {


    private final HttpServiceProxyFactory factory;


    public PageServiceBroker(HttpServiceProxyFactory factory) {
        this.factory = factory;
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

    /**
     * 创建页面
     * @param requestBody 页面数据
     * @return 页面数据
     */
    public NotionReact<Object> createPage(String requestBody) {
        try {
            log.info("创建Notion页面");
            PageService service = factory.createClient(PageService.class);
            ResponseEntity<PageData> response = service.createPage(requestBody);
            log.info("创建Notion页面成功");
            return new NotionReact<>(response.getStatusCode().value(), "创建页面成功", response);
        } catch (NotionResponseException e) {
            log.error("创建Notion页面返回失败，错误码：{}，错误信息：{}", e.getCode(), e.getMessage());
            return new NotionReact<>(e.getCode(), "创建Notion页面返回失败", e.getMessage());
        } catch (Exception e) {
            log.error("创建Notion页面返回失败，错误信息：{}", e.getMessage());
            return new NotionReact<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "创建Notion页面返回失败", e.getMessage());
        }
    }

}
