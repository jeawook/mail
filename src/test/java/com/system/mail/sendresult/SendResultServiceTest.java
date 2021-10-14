package com.system.mail.sendresult;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendResultServiceTest {

    @Autowired
    private SendResultService sendResultService;
    @Test
    @DisplayName("발송 결과 생성 테스트")
    void sendResultServiceSaveTest(){

    }
}