package com.system.mail.support;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.Status;
import com.system.mail.user.User;

import java.time.LocalDateTime;

/**
 * 테스트 전반에서 반복되는 MailAddress/MailGroup/MailInfo/SendInfo 픽스처 생성 보일러플레이트를 모아둔 헬퍼.
 */
public final class MailFixtures {

    private MailFixtures() {
    }

    public static MailAddress mailAddress(String name, String email) {
        return MailAddress.builder().name(name).email(email).build();
    }

    public static MailAddress noReplyAddress() {
        return mailAddress("no_reply", "pdj13579@nate.com");
    }

    public static MailAddress customerAddress() {
        return mailAddress("고객", "pdj13579@nate.com");
    }

    public static User user(MailAddress address, String macroValue) {
        return User.builder().mailAddress(address).macroValue(macroValue).build();
    }

    public static MailInfo mailInfo(MailAddress from, ContentEncoding encoding, String mailInfoName) {
        return MailInfo.builder()
                .mailFrom(from)
                .replyTo(from)
                .charset("utf-8")
                .encoding(encoding)
                .contentType(ContentType.HTML)
                .mailInfoName(mailInfoName)
                .build();
    }

    public static MailInfo mailInfo(MailAddress from, ContentEncoding encoding) {
        return mailInfo(from, encoding, "테스트 설정");
    }

    public static MailInfo mailInfo(MailAddress from, String mailInfoName) {
        return mailInfo(from, ContentEncoding.BASE64, mailInfoName);
    }

    public static MailInfo mailInfo(MailAddress from) {
        return mailInfo(from, ContentEncoding.BASE64);
    }

    public static MailGroup mailGroup(String macroKey) {
        return MailGroup.builder().mailGroupName("테스트 그룹").macroKey(macroKey).build();
    }

    public static MailGroup mailGroupWithUser(MailAddress userAddress) {
        MailGroup mailGroup = mailGroup("macro1,macro2");
        mailGroup.addUser(user(userAddress, "안녕하세요,10000"));
        return mailGroup;
    }

    public static SendInfo.SendInfoBuilder sendInfoBuilder(String subject, String content, MailInfo mailInfo) {
        return SendInfo.builder()
                .subject(subject)
                .content(content)
                .status(Status.WAIT)
                .sendDate(LocalDateTime.now())
                .mailInfo(mailInfo);
    }
}
