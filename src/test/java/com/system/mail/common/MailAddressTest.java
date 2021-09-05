package com.system.mail.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class MailAddressTest {

    @Test
    void createMailAddressTest() {
        MailAddress mailAddress = MailAddress.builder("Test", "test").build();
    }

}