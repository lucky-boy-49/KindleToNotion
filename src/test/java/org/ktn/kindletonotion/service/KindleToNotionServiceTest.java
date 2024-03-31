package org.ktn.kindletonotion.service;

import org.junit.jupiter.api.Test;
import org.ktn.kindletonotion.model.notion.Callout;
import org.ktn.kindletonotion.model.request.RequestBodyCallout;

class KindleToNotionServiceTest {

    @Test
    void uploadBookNote() {
        Callout callout = new Callout();
        callout.getRichText()[0].getText().setContent("测试");
        String requestBody = new RequestBodyCallout(callout).toString();
        System.out.println(requestBody);
    }
}