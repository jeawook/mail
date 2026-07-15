package com.system.mail.mailprocessor;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoRepository;
import com.system.mail.sendinfo.Status;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import com.system.mail.sendresultdetail.SendResultDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.system.mail.support.MailFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MailProcessorTest {

    @Autowired
    private MailInfoRepository mailInfoRepository;
    @Autowired
    private MailGroupRepository mailGroupRepository;
    @Autowired
    private SendInfoRepository sendInfoRepository;
    @Autowired
    private SendResultRepository sendResultRepository;
    @Autowired
    private MailProcessor mailProcessor;

    @Test
    @Transactional
    void mailSendTest() {
        String content = "메일 본문";
        String subject = "제목";
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailInfo mailInfo = mailInfo(noReplyAddress());
        SendInfo saveSendInfo = sendInfoBuilder(subject, content, mailInfo)
                .mailGroup(mailGroup)
                .build();
        SendResult sendResult = SendResult.builder().sendInfo(saveSendInfo).build();
        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        sendResultRepository.save(sendResult);
        SendInfo sendInfo = sendInfoRepository.save(saveSendInfo);
        mailProcessor.process(sendInfo.getId());

        Optional<SendInfo> byId = sendInfoRepository.findById(sendInfo.getId());
        SendInfo getSendInfo = byId.get();
        SendResult getSendResult = getSendInfo.getSendResult();
        List<SendResultDetail> sendResultDetails = getSendResult.getSendResultDetails();
        assertThat(getSendInfo.getStatus()).isEqualTo(Status.COMPLETE);
        assertThat(sendResultDetails.get(0).getResultCode()).isEqualTo("250");
    }

    @Test
    @Transactional
    void mailSendExceptionTest() {
        String content = "<html>\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<title></title>\n" +
                "<title></title>\n" +
                "<title>SW기술자 경력관리시스템 개선 및 이용안내</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<link href=\"http://made.sw.or.kr:8080/css/ko_mail.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "<div style=\"margin: 0px; padding: 0px; width: 100%;\">\n" +
                "<div style=\"margin: 0px auto; padding: 0px; width: 740px; overflow: hidden;\">\n" +
                "<table style=\"background: rgb(255, 255, 255); border-width: 0px; margin: 0px; padding: 0px; border-image: none; width: 740px; text-align: left;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "test<tbody>\n" +
                "<tr>\n" +
                "<!-- 뉴스레터 끝 -->\n" +
                "</body>\n" +
                "</html>\n";
        String subject = "제목";
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailInfo mailInfo = mailInfo(noReplyAddress(), ContentEncoding.DEFAULT);
        SendInfo saveSendInfo = sendInfoBuilder(subject, content, mailInfo)
                .mailGroup(mailGroup)
                .build();
        SendResult sendResult = SendResult.builder().sendInfo(saveSendInfo).build();
        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        sendResultRepository.save(sendResult);
        SendInfo sendInfo = sendInfoRepository.save(saveSendInfo);
        mailProcessor.process(sendInfo.getId());

        Optional<SendInfo> byId = sendInfoRepository.findById(sendInfo.getId());
        SendInfo getSendInfo = byId.get();
        SendResult getSendResult = getSendInfo.getSendResult();
        List<SendResultDetail> sendResultDetails = getSendResult.getSendResultDetails();
        assertThat(getSendInfo.getStatus()).isEqualTo(Status.COMPLETE);
        assertThat(sendResultDetails.get(0).getResultCode()).isEqualTo("250");
    }
}