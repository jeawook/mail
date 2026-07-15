package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.Status;
import com.system.mail.sendresultdetail.SendResultDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Base64;

import static com.system.mail.support.MailFixtures.mailAddress;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MailMessageBuilderTest {

    @Autowired
    private MailMessageBuilder mailMessageBuilder;

    @Test
    void buildAppliesHeadersAndMacroSubstitution() {
        MailAddress mailFrom = mailAddress("발신자", "no-reply@test.com");
        MailAddress rcpTo = mailAddress("수신자", "rcpt@test.com");

        MailInfo mailInfo = MailInfo.builder()
                .mailInfoName("테스트 설정")
                .mailFrom(mailFrom)
                .replyTo(mailFrom)
                .charset("utf-8")
                .encoding(ContentEncoding.DEFAULT)
                .contentType(ContentType.HTML)
                .build();

        SendInfo sendInfo = SendInfo.builder()
                .sendDate(LocalDateTime.now())
                .mailInfo(mailInfo)
                .subject("[$name$]님 안녕하세요")
                .content("본문입니다 [$name$]")
                .status(Status.WAIT)
                .macroKey("name")
                .build();

        SendResultDetail sendResultDetail = SendResultDetail.builder()
                .mailAddress(rcpTo)
                .macroValue("재욱")
                .build();

        String message = mailMessageBuilder.build(sendInfo, sendResultDetail);

        assertThat(message).contains("From: ");
        assertThat(message).contains("Reply-To: ");
        assertThat(message).contains("To: ");
        assertThat(message).contains("Content-Type: " + mailInfo.getHeaderContentType());
        assertThat(message).contains("Content-Transfer-Encoding: " + ContentEncoding.DEFAULT.getValue());
        // 매크로 치환이 본문에 적용됐는지(DEFAULT 인코딩이라 평문으로 확인 가능)
        assertThat(message).contains("본문입니다 재욱");
        assertThat(message).doesNotContain("[$name$]");
    }

    @Test
    void buildBase64EncodesContentWhenConfigured() {
        MailAddress mailFrom = mailAddress("발신자", "no-reply@test.com");
        MailAddress rcpTo = mailAddress("수신자", "rcpt@test.com");

        MailInfo mailInfo = MailInfo.builder()
                .mailInfoName("테스트 설정")
                .mailFrom(mailFrom)
                .replyTo(mailFrom)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64)
                .contentType(ContentType.HTML)
                .build();

        SendInfo sendInfo = SendInfo.builder()
                .sendDate(LocalDateTime.now())
                .mailInfo(mailInfo)
                .subject("제목")
                .content("본문")
                .status(Status.WAIT)
                .macroKey("")
                .build();

        SendResultDetail sendResultDetail = SendResultDetail.builder()
                .mailAddress(rcpTo)
                .macroValue("")
                .build();

        String message = mailMessageBuilder.build(sendInfo, sendResultDetail);

        String base64Body = Base64.getMimeEncoder().encodeToString("본문".getBytes());
        assertThat(message).contains(base64Body);
    }
}
