package org.ktn.kindletonotion.notion.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * 字符串工具类
 * @author 贺佳
 */
public class StringUtil {

    /**
     * 按指定长度分割字符串
     * @param input 输入字符串
     * @param splitLength 分割长度
     * @return 分割后的字符串列表
     */
    public static List<String> splitString(String input, int splitLength) {
        List<String> splitStrings = new LinkedList<>();

        int length = input.length();
        for (int i = 0; i < length; i += splitLength) {
            if (i + splitLength <= length) {
                splitStrings.add(input.substring(i, i + splitLength));
            } else {
                splitStrings.add(input.substring(i));
            }
        }

        return splitStrings;
    }

}
