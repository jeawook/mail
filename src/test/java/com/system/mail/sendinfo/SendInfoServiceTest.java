package com.system.mail.sendinfo;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.user.User;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.mailprocessor.ContentEncoding;
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
        MailAddress mail = MailAddress.builder("no_reply", "pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder("고객", "pdj13579@nate.com").build();
        MailGroup mailGroup = MailGroup.builder("테스트 그룹", "macro1,macro2").build();
        User user = User.builder(mailAddress, "안녕하세요,10000").build();
        mailGroup.addUser(user);

        MailInfo mailInfo = MailInfo.MailInfoBuilder()
                .mailFrom(mail)
                .replyTo(mail)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64.getValue())
                .contentType(ContentType.HTML)
                .mailInfoName("테스트 설정")
                .build();
        SendResult sendResult = SendResult.builder(mailGroup).build();
        sendResult.createSendResultDetails(mailGroup.getUsers());
        SendInfo sendInfo = SendInfo.builder("제목","본문",LocalDateTime.now(), Status.WAIT)
                .mailInfo(mailInfo)
                .sendDate(LocalDateTime.now())
                .mailGroup(mailGroup)
                .sendResult(sendResult)
                .build();
        MailGroup saveMailGroup = mailGroupRepository.save(mailGroup);
        MailInfo saveMailInfo = mailInfoRepository.save(mailInfo);
        SendResult saveSendResult = sendResultRepository.save(sendResult);

        SendInfo saveSendInfo = sendInfoService.saveSendInfo(sendInfo);
        assertThat(mailGroup).isEqualTo(saveMailGroup);
        assertThat(mailInfo).isEqualTo(saveMailInfo);
        assertThat(sendInfo).isEqualTo(saveSendInfo);
        assertThat(saveSendResult).isEqualTo(sendInfo.getSendResult());
        assertThat(saveMailInfo).isEqualTo(sendInfo.getMailInfo());
        assertThat(saveMailGroup).isEqualTo(sendInfo.getMailGroup());
        assertThat(saveSendResult.getSendResultDetails()).isEqualTo(saveSendInfo.getSendResult().getSendResultDetails());

    }

}