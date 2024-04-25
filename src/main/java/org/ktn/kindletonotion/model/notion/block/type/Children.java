package org.ktn.kindletonotion.model.notion.block.type;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

/**
 * 追加块时所使用的类型
 * @author jiahe
 */
@Getter
public class Children {

    private final ArrayNode children;

    public Children() {
        children = new ArrayNode(null);
    }

    public void setCallout(ObjectNode callout) {
        children.add(callout);
    }

    public void setDivider(ObjectNode divider) {
        children.add(divider);
    }

    public void setParagraph(ObjectNode paragraph) {
        children.add(paragraph);
    }

    public void setQuote(ObjectNode quote) {
        children.add(quote);
    }

}
