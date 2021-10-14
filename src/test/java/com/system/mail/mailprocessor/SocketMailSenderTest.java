package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailgroup.User;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoRepository;
import com.system.mail.sendinfo.Status;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SocketMailSenderTest {

    @Autowired
    private SocketMailSender socketMailSender;
    @Test
    @DisplayName("smtp 통신 테스트")
    void sendTest() {
        MailAddress rcpTo = MailAddress.builder("수신자", "pdj13579@nate.com").build();
        MailAddress mailFrom = MailAddress.builder("발신자", "pdj13579@nate.com").build();
        MailDTO mailDTO = MailDTO.MailDTOBuilder().mailFrom(mailFrom).rcpTo(rcpTo).data("테스트메일").build();
        SMTPResult smtpResult = socketMailSender.send(mailDTO);
        assertThat(smtpResult.getResultCode()).isEqualTo(SMTPCode.SUCCESS.getValue());
    }

    @Test
    @DisplayName("smtp server error 테스트")
    void sendExceptionTest() {
        MailAddress rcpTo = MailAddress.builder("수신자", "pdj13579@gwedsfw.com").build();
        MailAddress mailFrom = MailAddress.builder("발신자", "pdj13579@nate.com").build();
        MailDTO mailDTO = MailDTO.MailDTOBuilder().mailFrom(mailFrom).rcpTo(rcpTo).data("테스트메일").build();
        SMTPResult smtpResult = socketMailSender.send(mailDTO);
        assertThat(smtpResult.getResultCode()).isEqualTo(SMTPCode.SYSTEM_ERROR.getValue());
    }
}