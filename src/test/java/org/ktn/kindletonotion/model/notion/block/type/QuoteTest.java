package org.ktn.kindletonotion.model.notion.block.type;

import org.junit.jupiter.api.Test;
import org.ktn.kindletonotion.notion.utils.JsonUtil;

class QuoteTest {

    @Test
    void test() {
        Quote quote = new Quote("test");
        System.out.println(JsonUtil.toJson(quote));
    }

}