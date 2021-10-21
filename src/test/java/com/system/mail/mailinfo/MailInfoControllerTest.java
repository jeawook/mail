package com.system.mail.mailinfo;

import com.system.mail.common.MailAddress;
import com.system.mail.mailprocessor.ContentEncoding;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailInfoControllerTest {

    @Autowired
    private MailInfoService mailInfoService;

    @Test
    void mailInfoServiceSaveTest() {
        MailAddress mail = MailAddress.builder("no_reply", "test@email.com").build();

        String encoding = ContentEncoding.BASE64.getValue();
        ContentType contentType = ContentType.HTML;
        String charset = "utf-8";
        String mailInfoName = "테스트 설정";
        MailInfo mailInfo = MailInfo.MailInfoBuilder()
                .mailFrom(mail)
                .replyTo(mail)
                .charset(charset)
                .encoding(encoding)
                .contentType(contentType)
                .mailInfoName(mailInfoName)
                .build();
        MailInfo saveMailInfo = mailInfoService.saveMailInfo(mailInfo);
        assertThat(mailInfo).isEqualTo(saveMailInfo);
        assertThat(mailInfo.getMailFrom()).isEqualTo(mail);
        assertThat(mailInfo.getContentType()).isEqualTo(ContentType.HTML);
        assertThat(mailInfo.getEncoding()).isEqualTo(encoding);
        assertThat(mailInfo.getMailInfoName()).isEqualTo(mailInfoName);
    }

}