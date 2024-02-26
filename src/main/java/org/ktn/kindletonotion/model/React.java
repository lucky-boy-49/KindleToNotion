package org.ktn.kindletonotion.model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 响应信息
 * @param responseCode 响应码
 * @param responseMassage 响应信息
 * @param data 响应数据
 */
public record React(String responseCode, String responseMassage, JsonNode data) {
}
