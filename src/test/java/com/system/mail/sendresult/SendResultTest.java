package com.system.mail.sendresult;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static com.system.mail.support.MailFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SendResultTest {

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
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailInfo mailInfo = mailInfo(noReplyAddress());

        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        SendInfo sendInfo = sendInfoBuilder(subject, content, mailInfo)
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
        em.flush();
        em.clear();
        SendResult findSendResult = sendResultRepository.findById(saveSendResult.getId()).orElseThrow();
        assertThat(findSendResult.getMacroKey()).isEqualTo(sendResult.getMacroKey());
        assertThat(findSendResult.getSendResultDetails().size()).isEqualTo(sendResult.getSendResultDetails().size());
        assertThat(findSendResult.getSendInfo().getId()).isEqualTo(sendResult.getSendInfo().getId());
    }
}