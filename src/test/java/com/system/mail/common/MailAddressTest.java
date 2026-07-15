package com.system.mail.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MailAddressTest {

    @Test
    @DisplayName("mailAddress 생성 테스트")
    void createMailAddressTest() {
        String name = "이름";
        String email = "test@test.com";
        MailAddress mailAddress = MailAddress.builder().name(name).email(email).build();
        assertThat(mailAddress.getName()).isEqualTo(name);
        assertThat(mailAddress.getEmail()).isEqualTo(email);
        assertThat(mailAddress.getAddress()).isEqualTo("<"+email+">");
        assertThat(mailAddress.getHeaderAddress()).isEqualTo("\""+name+"\""+"<"+email+">");
    }

}