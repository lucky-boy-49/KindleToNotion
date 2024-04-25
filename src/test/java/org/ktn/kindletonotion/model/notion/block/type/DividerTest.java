package org.ktn.kindletonotion.model.notion.block.type;

import org.junit.jupiter.api.Test;
import org.ktn.kindletonotion.notion.utils.JsonUtil;


class DividerTest {

    @Test
    void test() {
        Divider divider = new Divider();
        System.out.println(JsonUtil.toJson(divider));
    }

}