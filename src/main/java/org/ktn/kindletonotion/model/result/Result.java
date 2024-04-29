package org.ktn.kindletonotion.model.result;

import lombok.Data;

import java.util.List;

/**
 * 上传结果
 * @author jiahe
 */
@Data
public class Result {

    private List<Success>  success;

    private List<Same> sames;

}
