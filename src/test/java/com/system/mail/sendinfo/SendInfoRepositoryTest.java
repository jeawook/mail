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


    private void createData() {
        MailAddress mail = MailAddress.builder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder().name("고객").email("pdj13579@nate.com").build();
        MailGroup mailGroup = getMailGroup(mailAddress);

        MailInfo mailInfo = getMailInfo(mail);

        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        for (int i = 0; i < 100; i++) {
            SendInfo sendInfo = SendInfo.builder()
                    .subject(subject+"_"+i)
                    .content(content+"_"+i)
                    .status(Status.WAIT)
                    .mailInfo(mailInfo)
                    .sendDate(nowDate)
                    .mailGroup(mailGroup)
                    .build();
            sendInfoRepository.save(sendInfo);
        }

        for (int i = 0; i < 100; i++) {
            SendInfo sendInfo = SendInfo.builder()
                    .subject(subject+"_"+i)
                    .content(content+"_"+i)
                    .status(Status.WAIT)
                    .mailInfo(mailInfo)
                    .sendDate(nowDate.plusDays(1))
                    .mailGroup(mailGroup)
                    .build();
            sendInfoRepository.save(sendInfo);
        }
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

    private MailGroup getMailGroup(MailAddress mailAddress) {
        MailGroup mailGroup = MailGroup.builder().mailGroupName("테스트 그룹").macroKey("macro1,macro2").build();
        User user = User.builder().mailAddress(mailAddress).macroValue("안녕하세요,10000").build();
        mailGroup.addUser(user);
        return mailGroup;
    }

    @Test
    void findBySendInfoDtoTest() {
        MailAddress mail = MailAddress.builder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.builder().name("고객").email("pdj13579@nate.com").build();
        MailGroup mailGroup = getMailGroup(mailAddress);

        MailInfo mailInfo = getMailInfo(mail);

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

        SendInfo save = sendInfoRepository.save(sendInfo);
        SendInfoDto dto = sendInfoRepository.findSendInfoDtoById(save.getId());

        assertThat(dto.getMailGroupName()).isEqualTo(mailGroup.getMailGroupName());
        assertThat(dto.getMailInfoName()).isEqualTo(mailInfo.getMailInfoName());
        assertThat(dto.getContent()).isEqualTo(save.getContent());
//        assertThat(dto.getSendDate()).isEqualTo(save.getSendDate());
        assertThat(dto.getSendResultId()).isNull();
    }

    @Test
    void findByDateAndSubjectTest() {
        createData();
        LocalDateTime startDate = LocalDateTime.of(nowDate.getYear(), nowDate.getMonth(), nowDate.getDayOfMonth(), 0, 0);

        PageRequest pageRequest = PageRequest.of(0, 10);
        LocalDateTime endDate = startDate.plusDays(1);
        Page<SendInfo> result = sendInfoRepository.findByDateAndSubject(pageRequest, subject, startDate, endDate);

        assertThat(result.getTotalPages()).isEqualTo(10);
        assertThat(result.getSize()).isEqualTo(10);

    }

    @Test
    void findByDateAndSubjectNullTest() {
        createData();
        LocalDateTime startDate = LocalDateTime.of(nowDate.getYear(), nowDate.getMonth(), nowDate.getDayOfMonth(), 0, 0);

        PageRequest pageRequest = PageRequest.of(0, 10);
        LocalDateTime endDate = startDate.plusDays(1);
        Page<SendInfo> result = sendInfoRepository.findByDateAndSubject(pageRequest, null, startDate, endDate);

        assertThat(result.getTotalPages()).isEqualTo(10);
        assertThat(result.getSize()).isEqualTo(10);
    }
    @Test
    void findByEndDateNullAndSubjectNullTest() {
        createData();
        LocalDateTime startDate = LocalDateTime.of(nowDate.getYear(), nowDate.getMonth(), nowDate.getDayOfMonth(), 0, 0);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<SendInfo> result = sendInfoRepository.findByDateAndSubject(pageRequest, null, startDate, null);

        assertThat(result.getTotalPages()).isEqualTo(20);
        assertThat(result.getSize()).isEqualTo(10);

    }
}