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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendInfoServiceTest {
    @Autowired
    private MailInfoRepository mailInfoRepository;
    @Autowired
    private MailGroupRepository mailGroupRepository;
    @Autowired
    private SendInfoRepository sendInfoRepository;
    @Autowired
    private SendResultRepository sendResultRepository;

    @Autowired
    private SendInfoService sendInfoService;
    @Test
    @DisplayName("발송 정보 생성 테스트")
    void sendInfoServiceSaveTest() {
        MailAddress mail = MailAddress.MailAddressBuilder().name("no_reply").email("test@email.com").build();
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
        sendResult.createSendResultDetails(mailGroup.getUsers());
        SendInfo sendInfo = SendInfo.SendInfoBuilder()
                .mailInfo(mailInfo)
                .status(Status.WAIT)
                .sendDate(LocalDateTime.now())
                .mailGroup(mailGroup)
                .sendResult(sendResult)
                .build();
        mailGroupRepository.save(mailGroup);
        mailInfoRepository.save(mailInfo);
        sendResultRepository.save(sendResult);
        sendInfoRepository.save(sendInfo);



    }

}