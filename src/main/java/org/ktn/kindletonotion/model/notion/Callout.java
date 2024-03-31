package org.ktn.kindletonotion.model.notion;

import lombok.Getter;
import lombok.Setter;

/**
 * 更新Callout时使用
 * @author 贺佳
 */
@Setter
@Getter
public class Callout {

    private RichText[] richText;
    private Icon icon;
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

    @Setter
    @Getter
    public static class Icon {

        private String type;
        private String emoji;

        public Icon() {
            this.type = "emoji";
            this.emoji = "💡";
        }
    }

    public Callout() {
        this.richText = new RichText[1];
        this.richText[0] = new RichText();
        this.icon = new Icon();
        this.color = "gray_background";
    }
}
