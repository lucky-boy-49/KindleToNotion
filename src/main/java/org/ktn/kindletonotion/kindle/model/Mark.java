package org.ktn.kindletonotion.kindle.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标记模型
 * @author 贺佳
 */
@Data
@AllArgsConstructor
public class Mark {

    /**
     * 标记时间
     */
    private LocalDateTime time;

    /**
     * 标记时间字符串
     */
    private String timeString;

    /**
     * 笔记位置
     */
    private String address;

    /**
     * 笔记内容
     */
    private String content;

    /**
     * 笔记评论
     */
    private String note;

    /**
     * 是否有评论
     */
    private Boolean haveNote;

}
