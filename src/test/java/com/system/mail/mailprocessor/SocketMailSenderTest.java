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
        // 과거 조건 반전 버그(HELO 응답 직후 바로 예외가 발생하고 그 예외의 code가 우연히 250이라
        // "성공"으로 오인되던 문제)의 재발을 잡기 위한 회귀 가드. 실제로 QUIT까지 정상 완료됐다면
        // resultMessage 에 SMTPException 메시지가 섞여 있으면 안 된다.
        assertThat(smtpResult.getResultMessage()).doesNotContain("Smtp protocol Exception");
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