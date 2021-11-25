package com.system.mail.sendinfo;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendInfoTest {

    @Test
    void sendInfoCreateTest() {
        String content = "메일 본문";
        String subject = "제목";
        Status status = Status.WAIT;
        LocalDateTime sendDate = LocalDateTime.now();
        MailAddress mail = MailAddress.builder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder().name("고객").email("pdj13579@nate.com").build();

        MailGroup mailGroup = getMailGroup(mailAddress);

        MailInfo mailInfo = getMailInfo(mail);
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
        MailAddress mail = MailAddress.builder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder().name("고객").email("pdj13579@nate.com").build();

        MailGroup mailGroup = getMailGroup(mailAddress);

        MailInfo mailInfo = getMailInfo(mail);

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

        assertThatThrownBy(() -> SendInfo.builder()
                .subject(subject)
                .content(content)
                .status(status)
                .sendDate(sendDate)
                .mailInfo(mailInfo)
                .mailGroup(null)
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("mailGroup must not be null");

     }

    @Test
    public void sendInfoStatusTest() {
        String content = "메일 본문";
        String subject = "제목";
        Status status = Status.WAIT;
        LocalDateTime sendDate = LocalDateTime.now();
        MailAddress mail = MailAddress.builder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder().name("고객").email("pdj13579@nate.com").build();

        MailGroup mailGroup = getMailGroup(mailAddress);

        MailInfo mailInfo = getMailInfo(mail);
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

    private MailGroup getMailGroup(MailAddress mailAddress) {
        MailGroup mailGroup = MailGroup.builder().mailGroupName("테스트 그룹").macroKey("macro1,macro2").build();
        User user = User.builder().mailAddress(mailAddress).macroValue("안녕하세요,10000").build();
        mailGroup.addUser(user);
        return mailGroup;
    }

    private MailInfo getMailInfo(MailAddress mail) {
        MailInfo mailInfo = MailInfo.builder()
                .mailFrom(mail)
                .replyTo(mail)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64)
                .contentType(ContentType.HTML)
                .mailInfoName("테스트 설정")
                .build();
        return mailInfo;
    }

}