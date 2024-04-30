package org.ktn.kindletonotion.model;

import lombok.Getter;

/**
 * 结果枚举
 * @author jiahe
 */
@Getter
public enum ReactEnum {


    SUCCESS(0, "上传成功"),
    FAILURE(1, "上传失败"),
    NUMBER_NOTES_IS_SAME(2, "笔记数相同");

    private final int code;
    private final String message;

    ReactEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
