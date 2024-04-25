package org.ktn.kindletonotion.model.notion.block.type;

import org.junit.jupiter.api.Test;
import org.ktn.kindletonotion.notion.utils.JsonUtil;

class ParagraphTest {

    @Test
    void test() {
        Paragraph paragraph = new Paragraph("test");
        System.out.println(JsonUtil.toJson(paragraph));
        paragraph = new Paragraph("test", "");
        System.out.println(JsonUtil.toJson(paragraph));
    }

}