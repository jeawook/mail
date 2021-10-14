package com.system.mail.mailinfo;

import com.system.mail.common.MailAddress;
import com.system.mail.mailprocessor.ContentEncoding;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailInfoServiceTest {

    @Autowired
    private MailInfoService mailInfoService;
    @Test
    @DisplayName("메일 정보 생성 테스트")
    void mailInfoServiceSaveTest () {
        MailAddress mailAddress = MailAddress.MailAddressBuilder().name("no_reply").email("test@email.com").build();
        MailInfo mailInfo = MailInfo.MailInfoBuilder()
                .mailFrom(mailAddress)
                .replyTo(mailAddress)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64.getValue())
                .contentType(ContentType.HTML)
                .mailInfoName("테스트 설정")
                .build();
        MailInfo saveMailInfo = mailInfoService.saveMailInfo(mailInfo);
        assertThat(mailInfo).isEqualTo(saveMailInfo);
        assertThat(mailInfo.getMailInfoName()).isEqualTo(saveMailInfo.getMailInfoName());
        assertThat(mailInfo.getMailFrom()).isEqualTo(saveMailInfo.getMailFrom());
        assertThat(mailInfo.getEncoding()).isEqualTo(saveMailInfo.getEncoding());
        assertThat(mailInfo.getContentType()).isEqualTo(saveMailInfo.getContentType());
        assertThat(mailInfo.getCharset()).isEqualTo(saveMailInfo.getCharset());
    }

}