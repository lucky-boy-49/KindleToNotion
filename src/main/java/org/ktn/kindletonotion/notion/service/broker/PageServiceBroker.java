package org.ktn.kindletonotion.notion.service.broker;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.stereotype.Service;

/**
 * 页面操作
 * @author 贺佳
 */
@Slf4j
@Service
public class PageServiceBroker {

    private final NotionConfigProperties notionConfigProps;


    private final HttpHeaderUtil httpHeaderUtil;

    public PageServiceBroker(NotionConfigProperties notionConfigProps, HttpHeaderUtil httpHeaderUtil) {
        this.notionConfigProps = notionConfigProps;
        this.httpHeaderUtil = httpHeaderUtil;
    }


}
