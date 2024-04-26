package org.ktn.kindletonotion.Exception;

import lombok.Getter;
import reactor.core.publisher.Mono;

/**
 * Notion请求响应失败时的异常类
 * @author jiahe
 */
@Getter
public class NotionResponseException extends RuntimeException {

    private final int code;

    public NotionResponseException(int code, Mono<String> message) {
        super(message.block());
        this.code = code;
    }

}
