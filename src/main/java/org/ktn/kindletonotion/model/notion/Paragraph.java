package org.ktn.kindletonotion.model.notion;

import lombok.Getter;
import lombok.Setter;

/**
 * 更新Callout时使用
 * @author 贺佳
 */
@Setter
@Getter
public class Paragraph {

    private RichText[] richText;
    private String color;

    @Setter
    @Getter
    public static class RichText {
        private Text text;

        public RichText() {
            this.text = new Text();
        }

        @Setter
        @Getter
        public static class Text {
            private String content;
        }

    }

    public Paragraph() {
        this.richText = new RichText[1];
        this.richText[0] = new RichText();
        this.color = "default";
    }
}
