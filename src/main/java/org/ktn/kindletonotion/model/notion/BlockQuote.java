package org.ktn.kindletonotion.model.notion;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 贺佳
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BlockQuote extends BlockAbstract {

    private String object = "block";
    private String type = "quote";
    private Quote quote;

    public BlockQuote() {
        this.quote = new Quote();
    }

}
