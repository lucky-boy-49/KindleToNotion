package org.ktn.kindletonotion.model;

/**
 * 调用Notion API时响应模型
 * @param code 响应码
 * @param message 响应消息
 * @param data 响应数据
 * @param <T> 响应数据类型
 * @author jiahe
 */
public record NotionReact<T>(int code, String message, T data) {
}
