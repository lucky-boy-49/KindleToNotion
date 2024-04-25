package org.ktn.kindletonotion.model.notion.block.type;

import org.junit.jupiter.api.Test;
import org.ktn.kindletonotion.notion.utils.JsonUtil;

class CalloutTest {

    @Test
    void test() {
        Callout callout = new Callout("测试");
        System.out.println(JsonUtil.toJson(callout));
        Callout callout2 = new Callout("测试", "gray_background");
        System.out.println(JsonUtil.toJson(callout2));
    }

}