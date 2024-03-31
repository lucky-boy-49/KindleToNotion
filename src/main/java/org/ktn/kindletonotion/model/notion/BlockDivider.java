package org.ktn.kindletonotion.model.notion;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 贺佳
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BlockDivider extends BlockAbstract {

    private String object = "block";
    private String type = "divider";
    private Divider divider;

}
