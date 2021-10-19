package com.system.mail.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MailAddressTest {

    @Test
    @DisplayName("mailAddress 생성 테스트")
    void createMailAddressTest() {
        String name = "이름";
        String email = "test@test.com";
        MailAddress mailAddress = MailAddress.builder(name, email).build();
        assertThat(mailAddress.getName()).isEqualTo(name);
        assertThat(mailAddress.getEmail()).isEqualTo(email);
        assertThat(mailAddress.getAddress()).isEqualTo("<"+email+">");
        assertThat(mailAddress.getHeaderAddress()).isEqualTo("\""+name+"\""+"<"+email+">");
    }

    @Test
    void createMailAddressExceptionTest() {
        String name = "이름";
        String email = "test";
        MailAddress mailAddress = MailAddress.builder(null, email).build();
    }

}