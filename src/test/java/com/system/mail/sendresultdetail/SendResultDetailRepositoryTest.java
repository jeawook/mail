package com.system.mail.sendresultdetail;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoRepository;
import com.system.mail.sendinfo.Status;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import com.system.mail.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SendResultDetailRepositoryTest {

    @Autowired
    SendResultDetailRepository sendResultDetailRepository;
    @Autowired
    SendResultRepository sendResultRepository;
    @Autowired
    SendInfoRepository sendInfoRepository;
    @Autowired
    MailInfoRepository mailInfoRepository;
    @Autowired
    MailGroupRepository mailGroupRepository;
    @Autowired
    EntityManager em;

    private final static String subject = "subject";
    private final static String content = "content";
    private final static LocalDateTime nowDate = LocalDateTime.now();



    private SendInfo getSendInfo() {
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

        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        SendInfo sendInfo = SendInfo.builder()
                .subject(subject)
                .content(content)
                .status(Status.WAIT)
                .mailInfo(mailInfo)
                .sendDate(nowDate)
                .mailGroup(mailGroup)
                .build();
        return sendInfoRepository.save(sendInfo);
    }

    @Test
    void saveTest() {

        SendInfo sendInfo = getSendInfo();

        SendResult sendResult = SendResult.builder().sendInfo(sendInfo).build();
        SendResult saveSendResult = sendResultRepository.save(sendResult);

        em.clear();
        SendResult findSendResult = sendResultRepository.findById(saveSendResult.getId()).orElseGet(null);
        assertThat(findSendResult.getMacroKey()).isEqualTo(sendResult.getMacroKey());
        assertThat(findSendResult.getSendResultDetails().size()).isEqualTo(sendResult.getSendResultDetails().size());
        assertThat(findSendResult.getSendInfo().getId()).isEqualTo(sendResult.getSendInfo().getId());

    }

}