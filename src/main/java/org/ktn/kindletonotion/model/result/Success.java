package org.ktn.kindletonotion.model.result;

/**
 * 成功上传的书
 * @param bookName 书名
 * @param message 上传成功的消息
 */
public record Success(String bookName, String message) {
}
