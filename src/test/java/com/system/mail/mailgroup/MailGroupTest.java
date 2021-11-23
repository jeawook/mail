package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import com.system.mail.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MailGroupTest {
    @Test
    void mailGroupCreateTest() {
        String macroKey = "subject, content";
        String mailGroupName = "메일 그룹";
        String email = "test@test.com";
        String name = "ttest";
        String macroValue = "제목, 본문";
        MailAddress mailAddress = MailAddress.builder().name(name).email(email).build();
        MailGroup mailGroup = MailGroup.builder().mailGroupName(mailGroupName).macroKey(macroKey).build();
        User user = User.builder().mailAddress(mailAddress).macroValue(macroValue).build();
        mailGroup.addUser(user);

        assertThat(mailGroup.getUsers().size()).isEqualTo(1);
        assertThat(mailGroup.getMacroKey()).isEqualTo(macroKey);
        assertThat(mailGroup.getMailGroupName()).isEqualTo(mailGroupName);
        assertThat(mailGroup.getUsers()).extracting("email").containsExactly(email);

    }

}