package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import com.system.mail.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailGroupTest {
    @Test
    void mailGroupCreateTest() {
        MailAddress mailAddress = MailAddress.builder("ttest", "test@test.com").build();
        MailGroup mailGroup = MailGroup.builder("메일 그룹", "subject, content").build();
        User user = User.builder(mailAddress, "제목, 본문").build();
        mailGroup.addUser(user);

        Assertions.assertThat(mailGroup.getUsers().size()).isEqualTo(1);
    }

}