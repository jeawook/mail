package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailGroupServiceTest {

    @Autowired
    private MailGroupService mailGroupService;
    @Test
    void mailGroupServiceSaveTest() {
        MailAddress mailAddress = MailAddress.MailAddressBuilder().name("고객").email("pdj13579@nate.com").build();
        MailGroup mailGroup = MailGroup.MailGroupBuilder().mailGroupName("테스트 그룹").macroKey("macro1,macro2").build();
        User user = User.builder().mailAddress(mailAddress).macroValue("안녕하세요,10000").build();
        mailGroup.addUser(user);
        MailGroup saveMailGroup = mailGroupService.saveMailGroup(mailGroup);
        assertThat(mailGroup).isEqualTo(saveMailGroup);
        assertThat(user).isEqualTo(saveMailGroup.getUsers().get(0));
    }

}