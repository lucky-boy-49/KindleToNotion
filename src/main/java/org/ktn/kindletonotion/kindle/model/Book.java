package org.ktn.kindletonotion.kindle.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author 贺佳
 * 书籍模型
 */
@Data
@AllArgsConstructor
public class Book {

    private String name;

    private String author;

    private Integer nums;

    private List<Mark> marks;

    public void  markNumsSelfIncreasing() {
        nums++;
    }

}
