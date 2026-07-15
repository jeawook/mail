package com.system.mail.sendinfo;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupRepository;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static com.system.mail.support.MailFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        return mailInfo(mailAddress("no_reply", "test@email.com"), mailInfoName);
    }
}