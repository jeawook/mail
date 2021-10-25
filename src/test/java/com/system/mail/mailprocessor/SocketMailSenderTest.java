package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class SocketMailSenderTest {

    @Autowired
    private SocketMailSender socketMailSender;
    @Test
    @DisplayName("smtp 통신 테스트")
    void sendTest() {
        MailAddress rcpTo = MailAddress.builder("수신자", "pdj13579@nate.com").build();
        MailAddress mailFrom = MailAddress.builder("발신자","pdj13579@nate.com").build();
        MailDTO mailDTO = MailDTO.builder(rcpTo, mailFrom, "테스트메일").build();
        SMTPResult smtpResult = socketMailSender.send(mailDTO);
        assertThat(smtpResult.getResultCode()).isEqualTo(SMTPCode.SUCCESS.getValue());
    }

    @Test
    @DisplayName("smtp server error 테스트")
    void sendExceptionTest() {
        MailAddress rcpTo = MailAddress.builder("수신자", "pdj13579@nate1.com").build();
        MailAddress mailFrom = MailAddress.builder("발신자","pdj13579@nate.com").build();
        MailDTO mailDTO = MailDTO.builder(rcpTo, mailFrom, "테스트메일").build();
        SMTPResult smtpResult = socketMailSender.send(mailDTO);
        assertThat(smtpResult.getResultCode()).isEqualTo(SMTPCode.SYSTEM_ERROR.getValue());
    }
}