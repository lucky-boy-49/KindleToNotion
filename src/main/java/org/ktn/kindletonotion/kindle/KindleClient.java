package org.ktn.kindletonotion.kindle;

import org.ktn.kindletonotion.kindle.service.KindleService;
import org.springframework.stereotype.Component;

/**
 * Kindle客户端
 */
@Component
public class KindleClient {

    public final KindleService kindle;

    public KindleClient(KindleService kindle) {
        this.kindle = kindle;
    }

}
