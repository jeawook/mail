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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SendInfoRepositoryTest {
    @Autowired
    private SendInfoRepository sendInfoRepository;
    @Autowired
    private MailInfoRepository mailInfoRepository;
    @Autowired
    private MailGroupRepository mailGroupRepository;
    @Autowired
    private SendResultRepository sendResultRepository;

    private final static String subject = "subject";
    private final static String content = "content";
    private final static LocalDateTime nowDate = LocalDateTime.now();
    @BeforeEach
    void before() {
        MailAddress mail = MailAddress.builder("no_reply", "pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder("고객", "pdj13579@nate.com").build();
        MailGroup mailGroup = MailGroup.builder("테스트 그룹", "macro1,macro2").build();
        User user = User.builder(mailAddress, "안녕하세요,10000").build();
        mailGroup.addUser(user);

        MailInfo mailInfo = MailInfo.MailInfoBuilder()
                .mailFrom(mail)
                .replyTo(mail)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64)
                .contentType(ContentType.HTML)
                .mailInfoName("테스트 설정")
                .build();
        SendResult sendResult = SendResult.builder(mailGroup).build();
        sendResult.createSendResultDetails(mailGroup.getUsers());

        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        sendResultRepository.save(sendResult);
        for (int i = 0; i < 100; i++) {
            SendInfo sendInfo = SendInfo.builder(subject+"_"+i,content+"_"+i,LocalDateTime.now(), Status.WAIT)
                    .mailInfo(mailInfo)
                    .sendDate(nowDate)
                    .mailGroup(mailGroup)
                    .sendResult(sendResult)
                    .build();
            sendInfoRepository.save(sendInfo);
        }

        for (int i = 0; i < 100; i++) {
            SendInfo sendInfo = SendInfo.builder(subject+"_"+i,content+"_"+i,LocalDateTime.now(), Status.WAIT)
                    .mailInfo(mailInfo)
                    .sendDate(nowDate.plusDays(1))
                    .mailGroup(mailGroup)
                    .sendResult(sendResult)
                    .build();
            sendInfoRepository.save(sendInfo);
        }
    }
    @Test
    public void findByDateAndSubjectTest() {
        LocalDateTime startDate = LocalDateTime.of(nowDate.getYear(), nowDate.getMonth(), nowDate.getDayOfMonth(), 0, 0);

        PageRequest pageRequest = PageRequest.of(0, 10);
        LocalDateTime endDate = startDate.plusDays(1);
        Page<SendInfo> result = sendInfoRepository.findByDateAndSubject(pageRequest, subject, startDate, endDate);

        assertThat(result.getTotalPages()).isEqualTo(10);
        assertThat(result.getSize()).isEqualTo(10);

    }

    @Test
    public void findByDateAndSubjectNullTest() {
        LocalDateTime startDate = LocalDateTime.of(nowDate.getYear(), nowDate.getMonth(), nowDate.getDayOfMonth(), 0, 0);

        PageRequest pageRequest = PageRequest.of(0, 10);
        LocalDateTime endDate = startDate.plusDays(1);
        Page<SendInfo> result = sendInfoRepository.findByDateAndSubject(pageRequest, null, startDate, endDate);

        assertThat(result.getTotalPages()).isEqualTo(10);
        assertThat(result.getSize()).isEqualTo(10);
    }
    @Test
    public void findByEndDateNullAndSubjectNullTest() {
        LocalDateTime startDate = LocalDateTime.of(nowDate.getYear(), nowDate.getMonth(), nowDate.getDayOfMonth(), 0, 0);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<SendInfo> result = sendInfoRepository.findByDateAndSubject(pageRequest, null, startDate, null);

        assertThat(result.getTotalPages()).isEqualTo(20);
        assertThat(result.getSize()).isEqualTo(10);

    }
}