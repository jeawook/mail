package com.system.mail.mailprocessor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailHeaderEncoderTest {
    @Autowired
    MailHeaderEncoder mailHeaderEncoder;

    @Test
    void createTest() {
        MailHeader mailHeader = MailHeader.CHARSET;
        String charset = "utf-8";
        String header = mailHeaderEncoder.create(mailHeader.getValue(), charset);

        assertThat(header).isEqualTo(mailHeader.getValue()+": "+charset+"\r\n");
    }

    @Test
    void encodeTest() throws UnsupportedEncodingException {
        Base64.Encoder encoder = Base64.getEncoder();
        MailHeader mailHeader = MailHeader.SUBJECT;
        String value = "제목";
        String charset = "utf-8";

        String header = mailHeaderEncoder.encode(mailHeader.getValue(), value, charset);

        assertThat(header).isEqualTo(mailHeader.getValue()+": =?"+charset+"?B?"+new String(encoder.encode(value.getBytes(charset)))+"?=\r\n");
    }

}