package org.ktn.kindletonotion.model;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新Callout时使用
 * @author 贺佳
 */
@Setter
@Getter
public class Callout {

    private RichText[] rich_text;

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public Callout() {
        this.rich_text = new RichText[1];
    }
}
