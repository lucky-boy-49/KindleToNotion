package org.ktn.kindletonotion.notion.utils;

import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.springframework.http.HttpHeaders;

/**
 * http header util
 * @author jiahe
 */
public class HttpHeaderUtil {

    private final NotionConfigProperties notionConfigProps;

    public HttpHeaderUtil(NotionConfigProperties notionConfigProps) {
        this.notionConfigProps = notionConfigProps;
    }

    public HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Notion-Version", notionConfigProps.getApiVersion());
        headers.set("Authorization", notionConfigProps.getAuthToken());
        return headers;
    }

}
