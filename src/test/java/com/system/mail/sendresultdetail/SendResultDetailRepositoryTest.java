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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

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
        MailAddress replyTo = MailAddress.builder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailFrom1 = MailAddress.builder().name("고객").email("pdj13579@nate.com").build();

        MailGroup mailGroup = MailGroup.builder().mailGroupName("테스트 그룹").macroKey("macro1,macro2").build();
        User user = User.builder().mailAddress(mailFrom1).macroValue("안녕하세요,10000").build();
        for (int i = 0; i < 10; i++) {
            mailGroup.addUser(user);
        }
        
        MailAddress mailFrom2 = MailAddress.builder().name("회원").email("pdj13579@nate.com").build();
        User user2 = User.builder().mailAddress(mailFrom2).macroValue("안녕하세요,10000").build();
        for (int i = 0; i < 15; i++) {
            mailGroup.addUser(user2);
        }
        
        MailAddress mailFrom3 = MailAddress.builder().name("회원").email("test@nate.com").build();
        User user3 = User.builder().mailAddress(mailFrom3).macroValue("안녕하세요,10000").build();
        
        mailGroup.addUser(user3);
        

        MailInfo mailInfo = MailInfo.builder()
                .mailFrom(replyTo)
                .replyTo(replyTo)
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
        SendResultDetailSearchCond cond = SendResultDetailSearchCond.builder().type(ReusltSearchType.NAME).value("회원").build();
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
        SendResultDetailSearchCond cond = SendResultDetailSearchCond.builder().type(ReusltSearchType.EMAIL).value("test").build();
        Page<SendResultDetail> resultDetails = sendResultDetailRepository.findByNameOrEmail(saveSendResult.getId(),cond, pageRequest);

        assertThat(resultDetails.getSize()).isEqualTo(10);
        assertThat(resultDetails.getContent().size()).isEqualTo(1);
        assertThat(resultDetails.getTotalPages()).isEqualTo(1);
    }

}