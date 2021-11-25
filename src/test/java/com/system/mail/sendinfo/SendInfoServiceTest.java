package com.system.mail.sendinfo;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.user.User;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SendInfoServiceTest {
    @Autowired
    private MailInfoRepository mailInfoRepository;
    @Autowired
    private MailGroupRepository mailGroupRepository;
    @Autowired
    private SendResultRepository sendResultRepository;

    @Autowired
    private SendInfoService sendInfoService;

    @Test
    @DisplayName("발송 정보 생성 테스트")
    void sendInfoServiceSaveTest() {
        String content = "메일 본문";
        String subject = "제목";
        MailAddress mail = MailAddress.builder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder().name("고객").email("pdj13579@nate.com").build();
        MailGroup mailGroup = MailGroup.builder().mailGroupName("테스트 그룹").macroKey("macro1,macro2").build();
        User user = User.builder().mailAddress(mailAddress).macroValue("안녕하세요,10000").build();
        mailGroup.addUser(user);

        MailInfo mailInfo = MailInfo.builder()
                .mailFrom(mail)
                .replyTo(mail)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64)
                .contentType(ContentType.HTML)
                .mailInfoName("테스트 설정")
                .build();
        SendInfo sendInfo = SendInfo.builder()
                .subject(subject)
                .content(content)
                .sendDate(LocalDateTime.now())
                .status(Status.WAIT)
                .mailInfo(mailInfo)
                .sendDate(LocalDateTime.now())
                .mailGroup(mailGroup)
                .build();
        MailGroup saveMailGroup = mailGroupRepository.save(mailGroup);
        MailInfo saveMailInfo = mailInfoRepository.save(mailInfo);

        SendInfo saveSendInfo = sendInfoService.saveSendInfo(sendInfo);

        SendResult sendResult = SendResult.builder().sendInfo(saveSendInfo).build();
        SendResult saveSendResult = sendResultRepository.save(sendResult);

        assertThat(mailGroup).isEqualTo(saveMailGroup);
        assertThat(mailInfo).isEqualTo(saveMailInfo);
        assertThat(sendInfo).isEqualTo(saveSendInfo);
        assertThat(saveSendResult).isEqualTo(sendInfo.getSendResult());
        assertThat(saveMailInfo).isEqualTo(sendInfo.getMailInfo());
        assertThat(saveMailGroup).isEqualTo(sendInfo.getMailGroup());
        assertThat(saveSendResult.getSendResultDetails()).isEqualTo(saveSendInfo.getSendResult().getSendResultDetails());

    }

}