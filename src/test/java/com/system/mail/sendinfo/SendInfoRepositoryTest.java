package com.system.mail.sendinfo;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.system.mail.support.MailFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SendInfoRepositoryTest {
    @Autowired
    private SendInfoRepository sendInfoRepository;
    @Autowired
    private MailInfoRepository mailInfoRepository;
    @Autowired
    private MailGroupRepository mailGroupRepository;

    private final static String subject = "subject";
    private final static String content = "content";
    private final static LocalDateTime nowDate = LocalDateTime.now();


    private void createData() {
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailInfo mailInfo = mailInfo(noReplyAddress());

        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        for (int i = 0; i < 100; i++) {
            SendInfo sendInfo = sendInfoBuilder(subject+"_"+i, content+"_"+i, mailInfo)
                    .sendDate(nowDate)
                    .mailGroup(mailGroup)
                    .build();
            sendInfoRepository.save(sendInfo);
        }

        for (int i = 0; i < 100; i++) {
            SendInfo sendInfo = sendInfoBuilder(subject+"_"+i, content+"_"+i, mailInfo)
                    .sendDate(nowDate.plusDays(1))
                    .mailGroup(mailGroup)
                    .build();
            sendInfoRepository.save(sendInfo);
        }
    }

    @Test
    void findBySendInfoDtoTest() {
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailInfo mailInfo = mailInfo(noReplyAddress());

        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        SendInfo sendInfo = sendInfoBuilder(subject, content, mailInfo)
                .sendDate(nowDate)
                .mailGroup(mailGroup)
                .build();

        SendInfo save = sendInfoRepository.save(sendInfo);
        SendInfoDto dto = sendInfoRepository.findSendInfoDtoById(save.getId());

        assertThat(dto.getMailGroupName()).isEqualTo(mailGroup.getMailGroupName());
        assertThat(dto.getMailInfoName()).isEqualTo(mailInfo.getMailInfoName());
        assertThat(dto.getContent()).isEqualTo(save.getContent());
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