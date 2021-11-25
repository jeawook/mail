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
        MailAddress rcpTo = MailAddress.builder().name("수신자").email("pdj13579@nate.com").build();
        MailAddress mailFrom = MailAddress.builder().name("발신자").email("pdj13579@nate.com").build();
        MailDto mailDto = MailDto.builder().rcpTo(rcpTo).mailFrom(mailFrom).data("테스트메일").build();
        SMTPResult smtpResult = socketMailSender.send(mailDto);
        assertThat(smtpResult.getResultCode()).isEqualTo(SMTPCode.SUCCESS.getValue());
    }

    @Test
    @DisplayName("smtp server error 테스트")
    void sendExceptionTest() {
        MailAddress rcpTo = MailAddress.builder().name("수신자").email("pdj13579@rcpto.ttt").build();
        MailAddress mailFrom = MailAddress.builder().name("발신자").email("pdj13579@nate.com").build();
        MailDto mailDto = MailDto.builder().rcpTo(rcpTo).mailFrom(mailFrom).data("테스트메일").build();
        SMTPResult smtpResult = socketMailSender.send(mailDto);
        assertThat(smtpResult.getResultCode()).isEqualTo(SMTPCode.SERVER_ERROR.getValue());
    }
}