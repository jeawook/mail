package com.system.mail.sendinfo;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.MailInfo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.system.mail.support.MailFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendInfoTest {

    @Test
    void sendInfoCreateTest() {
        String content = "메일 본문";
        String subject = "제목";
        Status status = Status.WAIT;
        LocalDateTime sendDate = LocalDateTime.now();
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailInfo mailInfo = mailInfo(noReplyAddress());
        SendInfo sendInfo = SendInfo.builder()
                .subject(subject)
                .content(content)
                .sendDate(sendDate)
                .status(status)
                .mailInfo(mailInfo)
                .mailGroup(mailGroup)
                .build();

        assertThat(sendInfo.getContent()).isEqualTo(content);
        assertThat(sendInfo.getSubject()).isEqualTo(subject);
        assertThat(sendInfo.getStatus()).isEqualTo(status);
        assertThat(sendInfo.getSendDate()).isEqualTo(sendDate);
        assertThat(sendInfo.getMailInfo()).isEqualTo(mailInfo);
        assertThat(sendInfo.getMailGroup()).isEqualTo(mailGroup);
        assertThat(sendInfo.getMacroKey()).isEqualTo(mailGroup.getMacroKey());
    }

    @Test
    void sendInfoCreateIllegalArgumentExceptionTest() {
        String content = "메일 본문";
        String subject = "제목";
        Status status = Status.WAIT;
        LocalDateTime sendDate = LocalDateTime.now();
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailInfo mailInfo = mailInfo(noReplyAddress());

        assertThatThrownBy(() -> SendInfo.builder()
                .subject(null)
                .content(content)
                .sendDate(sendDate)
                .status(status)
                .mailInfo(mailInfo)
                .mailGroup(mailGroup)
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("subject must not be null");

        assertThatThrownBy(() -> SendInfo.builder()
                .subject(subject)
                .content(null)
                .sendDate(sendDate)
                .status(status)
                .mailInfo(mailInfo)
                .mailGroup(mailGroup)
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("content must not be null");

        assertThatThrownBy(() -> SendInfo.builder()
                .subject(subject)
                .content(content)
                .sendDate(null)
                .status(status)
                .mailInfo(mailInfo)
                .mailGroup(mailGroup)
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("sendDate must not be null");

        assertThatThrownBy(() -> SendInfo.builder()
                .subject(subject)
                .content(content)
                .status(status)
                .sendDate(sendDate)
                .mailInfo(null)
                .mailGroup(mailGroup)
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("mailInfo must not be null");

     }

    @Test
    void sendInfoCreateWithoutMailGroupTest() {
        // API 단발성 발송처럼 mailGroup 없이 macroKey 를 직접 전달하는 경우
        String content = "메일 본문";
        String subject = "제목";
        Status status = Status.REGISTER;
        LocalDateTime sendDate = LocalDateTime.now();
        MailInfo mailInfo = mailInfo(noReplyAddress());

        SendInfo sendInfo = SendInfo.builder()
                .subject(subject)
                .content(content)
                .sendDate(sendDate)
                .status(status)
                .mailInfo(mailInfo)
                .macroKey("name,grade")
                .build();

        assertThat(sendInfo.getMailGroup()).isNull();
        assertThat(sendInfo.getMacroKey()).isEqualTo("name,grade");
    }

    @Test
    public void sendInfoStatusTest() {
        String content = "메일 본문";
        String subject = "제목";
        Status status = Status.WAIT;
        LocalDateTime sendDate = LocalDateTime.now();
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailInfo mailInfo = mailInfo(noReplyAddress());
        SendInfo sendInfo = SendInfo.builder()
                .subject(subject)
                .content(content)
                .sendDate(sendDate)
                .status(status)
                .mailInfo(mailInfo)
                .mailGroup(mailGroup)
                .build();
        sendInfo.mailRegister();
        assertThat(sendInfo.getStatus()).isEqualTo(Status.REGISTER);
        sendInfo.mailStatusSending();
        assertThat(sendInfo.getStatus()).isEqualTo(Status.SENDING);
        sendInfo.mailStatusComplete();
        assertThat(sendInfo.getStatus()).isEqualTo(Status.COMPLETE);
    }

}