package com.system.mail.mailgroup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.system.mail.support.MailFixtures.customerAddress;
import static com.system.mail.support.MailFixtures.mailGroupWithUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MailGroupServiceTest {

    @Autowired
    private MailGroupService mailGroupService;
    @Test
    @DisplayName("메일 그룹 생성 테스트")
    void mailGroupServiceSaveTest() {
        MailGroup mailGroup = mailGroupWithUser(customerAddress());
        MailGroup saveMailGroup = mailGroupService.saveMailGroup(mailGroup);
        assertThat(mailGroup).isEqualTo(saveMailGroup);
        assertThat(mailGroup.getUsers().get(0)).isEqualTo(saveMailGroup.getUsers().get(0));
    }

}