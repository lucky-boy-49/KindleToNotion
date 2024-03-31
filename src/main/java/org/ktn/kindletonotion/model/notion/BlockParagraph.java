package org.ktn.kindletonotion.model.notion;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 贺佳
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BlockParagraph extends BlockAbstract {

    private String object = "block";
    private String type = "paragraph";
    private Paragraph paragraph;


    public BlockParagraph() {
        this.paragraph = new Paragraph();
    }

}
