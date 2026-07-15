package com.system.mail.sendresultdetail;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoRepository;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import com.system.mail.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static com.system.mail.support.MailFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        MailGroup mailGroup = mailGroup("macro1,macro2");

        User user = user(customerAddress(), "안녕하세요,10000");
        for (int i = 0; i < 10; i++) {
            mailGroup.addUser(user);
        }

        MailAddress mailFrom2 = mailAddress("회원", "pdj13579@nate.com");
        User user2 = user(mailFrom2, "안녕하세요,10000");
        for (int i = 0; i < 15; i++) {
            mailGroup.addUser(user2);
        }

        MailAddress mailFrom3 = mailAddress("회원", "test@nate.com");
        User user3 = user(mailFrom3, "안녕하세요,10000");
        mailGroup.addUser(user3);

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
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<SendResultDetail> resultDetails = sendResultDetailRepository.findByResultId(saveSendResult.getId(), pageRequest);

        assertThat(resultDetails.getSize()).isEqualTo(10);
        assertThat(resultDetails.getTotalPages()).isEqualTo(3);
    }

    @Test
    void searchByNameTest() {

        SendInfo sendInfo = getSendInfo();

        SendResult sendResult = SendResult.builder().sendInfo(sendInfo).build();
        SendResult saveSendResult = sendResultRepository.save(sendResult);
        em.flush();
        em.clear();
        PageRequest pageRequest = PageRequest.of(0, 10);
        SendResultDetailSearchCond cond = SendResultDetailSearchCond.builder().searchType(ResultSearchType.NAME).searchKey("회원").build();
        Page<SendResultDetail> resultDetails = sendResultDetailRepository.findByNameOrEmail(saveSendResult.getId(),cond, pageRequest);

        assertThat(resultDetails.getSize()).isEqualTo(10);
        assertThat(resultDetails.getTotalPages()).isEqualTo(2);
    }

    @Test
    void searchByEmailTest() {

        SendInfo sendInfo = getSendInfo();

        SendResult sendResult = SendResult.builder().sendInfo(sendInfo).build();
        SendResult saveSendResult = sendResultRepository.save(sendResult);
        em.flush();
        em.clear();
        PageRequest pageRequest = PageRequest.of(0, 10);
        SendResultDetailSearchCond cond = SendResultDetailSearchCond.builder().searchType(ResultSearchType.EMAIL).searchKey("test").build();
        Page<SendResultDetail> resultDetails = sendResultDetailRepository.findByNameOrEmail(saveSendResult.getId(),cond, pageRequest);

        assertThat(resultDetails.getSize()).isEqualTo(10);
        assertThat(resultDetails.getContent().size()).isEqualTo(1);
        assertThat(resultDetails.getTotalPages()).isEqualTo(1);
    }

}