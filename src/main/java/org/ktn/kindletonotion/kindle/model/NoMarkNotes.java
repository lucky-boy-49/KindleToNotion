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

    private String position;

    private String content;

}
