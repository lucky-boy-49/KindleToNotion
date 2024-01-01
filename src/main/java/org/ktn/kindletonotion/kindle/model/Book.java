package org.ktn.kindletonotion.kindle.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 贺佳
 * 书籍模型
 */
@Data
public class Book {

    private String name;

    private String author;

    private String url;

    private Integer nums;

    private List<Mark> marks = new ArrayList<>();

}
