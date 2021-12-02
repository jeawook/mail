package com.system.mail.sendinfo;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.mailinfo.ContentType;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import com.system.mail.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendInfoControllerTest {

    @Autowired
    SendInfoController sendInfoController;
    @Autowired
    MailInfoRepository mailInfoRepository;
    @Autowired
    MailGroupRepository mailGroupRepository;

    @Test
    void mailInfoListTest() {
        MailInfo mailInfo = createMailInfo("테스트 그룹");
        MailInfo save = mailInfoRepository.save(mailInfo);
        Map<Long, String> map = sendInfoController.mailInfoList();

        assertThat(map.get(save.getId())).isNotNull();
        assertThat(map.get(save.getId())).isEqualTo(save.getMailInfoName());
    }

    @Test
    void mailGroupListTest() {
        MailGroup mailGroup = createMailGroup();
        Map<Long, String> map = sendInfoController.mailGroupList();

        assertThat(map.get(mailGroup.getId())).isNotNull();
        assertThat(map.get(mailGroup.getId())).isEqualTo(mailGroup.getMailGroupName());
    }

    private MailGroup createMailGroup() {
        return mailGroupRepository.save(MailGroup.builder().mailGroupName("테스트_그룹").macroKey("").build());
    }

    private MailInfo createMailInfo(String mailInfoName) {
        MailAddress mail = MailAddress.builder().name("no_reply").email("test@email.com").build();
        MailInfo mailInfo = MailInfo.builder()
                .mailFrom(mail)
                .replyTo(mail)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64)
                .contentType(ContentType.HTML)
                .mailInfoName(mailInfoName)
                .build();
        return mailInfo;
    }
}