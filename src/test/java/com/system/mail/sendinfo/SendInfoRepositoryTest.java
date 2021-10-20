package com.system.mail.sendinfo;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailgroup.User;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.mailprocessor.ContentEncoding;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendInfoRepositoryTest {
    @Autowired
    private SendInfoRepository sendInfoRepository;
    @Autowired
    private MailInfoRepository mailInfoRepository;
    @Autowired
    private MailGroupRepository mailGroupRepository;
    @Autowired
    private SendResultRepository sendResultRepository;
    @Test
    @Transactional
    void findSendInfoByStatusWaitTest() {
        String content = "메일 본문";
        String subject = "제목";
        MailAddress mail = MailAddress.builder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder().name("고객").email("pdj13579@nate.com").build();
        MailGroup mailGroup = MailGroup.builder().mailGroupName("테스트 그룹").macroKey("macro1,macro2").build();
        User user = User.builder().mailAddress(mailAddress).macroValue("안녕하세요,10000").build();
        mailGroup.addUser(user);

        MailInfo mailInfo = MailInfo.MailInfoBuilder()
                .mailFrom(mail)
                .replyTo(mail)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64.getValue())
                .contentType(ContentType.HTML)
                .mailInfoName("테스트 설정")
                .build();
        SendResult sendResult = SendResult.builder().mailGroup(mailGroup).totalCnt(1).completedCnt(0).build();
        SendInfo saveSendInfo = SendInfo.builder()
                .mailInfo(mailInfo)
                .subject(subject)
                .status(Status.WAIT)
                .sendDate(LocalDateTime.now())
                .mailGroup(mailGroup)
                .sendResult(sendResult)
                .content(content)
                .build();
        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        sendResultRepository.save(sendResult);
        sendInfoRepository.save(saveSendInfo);
        SendInfo byStatusWait = sendInfoRepository.findByStatusWait();
        Assertions.assertThat(byStatusWait).isEqualTo(saveSendInfo);
    }


}