package org.ktn.kindletonotion.model.notion;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 贺佳
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BlockCallout extends BlockAbstract {

    private String object = "block";
    private String type = "callout";
    private Callout callout;

    public BlockCallout() {
        this.callout = new Callout();
    }

}
