package com.system.mail.mailinfo;

import com.system.mail.common.MailAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.system.mail.support.MailFixtures.mailAddress;
import static com.system.mail.support.MailFixtures.mailInfo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MailInfoControllerTest {

    @Autowired
    private MailInfoService mailInfoService;

    @Test
    void mailInfoServiceSaveTest() {
        String mailInfoName = "테스트 설정";
        MailAddress mail = mailAddress("no_reply", "test@email.com");
        MailInfo mailInfo = mailInfo(mail, mailInfoName);
        MailInfo saveMailInfo = mailInfoService.saveMailInfo(mailInfo);
        assertThat(mailInfo).isEqualTo(saveMailInfo);
        assertThat(mailInfo.getMailFrom()).isEqualTo(mail);
        assertThat(mailInfo.getContentType()).isEqualTo(ContentType.HTML);
        assertThat(mailInfo.getEncoding()).isEqualTo(ContentEncoding.BASE64);
        assertThat(mailInfo.getMailInfoName()).isEqualTo(mailInfoName);
    }

}