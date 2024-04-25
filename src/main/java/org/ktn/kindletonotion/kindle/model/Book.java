package org.ktn.kindletonotion.kindle.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 书籍模型
 * @author 贺佳
 */
@Data
@AllArgsConstructor
public class Book {

    /**
     * 书籍名
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 笔记数
     */
    private Integer nums;

    /**
     * 笔记列表
     */
    private List<Mark> marks;

    /**
     * 增加笔记数
     */
    public void  markNumsSelfIncreasing() {
        nums++;
    }

}
