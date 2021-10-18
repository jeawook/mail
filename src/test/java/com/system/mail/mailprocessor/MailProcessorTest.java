package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailgroup.User;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoRepository;
import com.system.mail.sendinfo.Status;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import com.system.mail.sendresultdetail.SendResultDetail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

    static SendInfo sendInfo;
    @BeforeEach
    void beforeEach() {
        String content = "메일 본문";
        MailAddress mail = MailAddress.MailAddressBuilder().name("no_reply").email("pdj13579@nate.com").build();
        MailAddress mailAddress = MailAddress.MailAddressBuilder().name("고객").email("pdj13579@nate.com").build();
        MailGroup mailGroup = MailGroup.MailGroupBuilder().mailGroupName("테스트 그룹").macroKey("macro1,macro2").build();
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
        SendResult sendResult = SendResult.SendResult(mailGroup).build();
        SendInfo saveSendInfo = SendInfo.SendInfoBuilder()
                .mailInfo(mailInfo)
                .status(Status.WAIT)
                .sendDate(LocalDateTime.now())
                .mailGroup(mailGroup)
                .sendResult(sendResult)
                .content(content)
                .build();
        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        sendResultRepository.save(sendResult);
        sendInfo = sendInfoRepository.save(saveSendInfo);
    }

    @Test
    void mailSendTest() {
        mailProcessor.process(sendInfo.getId());
        Optional<SendInfo> byId = sendInfoRepository.findById(sendInfo.getId());
        SendInfo sendInfo = byId.get();
        SendResult sendResult = sendInfo.getSendResult();
        List<SendResultDetail> sendResultDetails = sendResult.getSendResultDetails();
        assertThat(sendInfo.getStatus()).isEqualTo(Status.COMPLETE);
        assertThat(sendResultDetails.get(0).getResultCode()).isEqualTo("250");
    }

}