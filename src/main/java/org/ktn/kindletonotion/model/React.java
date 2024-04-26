package org.ktn.kindletonotion.model;

/**
 * 响应信息
 * @param responseCode 响应码
 * @param responseMassage 响应信息
 * @param data 响应数据
 */
public record React(int responseCode, String responseMassage, Object data) {
}
