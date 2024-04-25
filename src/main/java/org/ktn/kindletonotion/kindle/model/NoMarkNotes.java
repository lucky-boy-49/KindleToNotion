package org.ktn.kindletonotion.kindle.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 没有找到标注的笔记
 * @author 贺佳
 */
@Data
@AllArgsConstructor
public class NoMarkNotes {

    /**
     * 笔记的位置
     */
    private String position;

    /**
     * 评论的内容
     */
    private String content;

}
