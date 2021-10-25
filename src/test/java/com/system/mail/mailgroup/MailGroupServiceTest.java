package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import com.system.mail.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MailGroupServiceTest {

    @Autowired
    private MailGroupService mailGroupService;
    @Test
    @DisplayName("메일 그룹 생성 테스트")
    void mailGroupServiceSaveTest() {
        MailAddress mailAddress = MailAddress.builder("고객", "pdj13579@nate.com").build();
        MailGroup mailGroup = MailGroup.builder("테스트 그룹", "macro1,macro2").build();
        User user = User.builder(mailAddress, "안녕하세요,10000").build();
        mailGroup.addUser(user);
        MailGroup saveMailGroup = mailGroupService.saveMailGroup(mailGroup);
        assertThat(mailGroup).isEqualTo(saveMailGroup);
        assertThat(user).isEqualTo(saveMailGroup.getUsers().get(0));
    }

}