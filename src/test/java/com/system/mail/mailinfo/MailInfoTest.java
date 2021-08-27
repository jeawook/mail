package com.system.mail.mailinfo;

import com.system.mail.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class MailInfoTest {

    @DisplayName("mailInfo생성테스트")
    @Test
    void createMailInfoTest() {
        MailAddress mailAddress = MailAddress.builder().address("jwpark@infomail.co.kr").name("박재욱").build();

        HashMap<String, String> macro = new HashMap<>();
        macro.put("name", "박재욱");
        MailGroup mailGroup = MailGroup.builder()
                .mailAddress(mailAddress)
                .macro(macro)
                .build();
        Assertions.assertThat(mailGroup.getMailAddress()).isEqualTo(mailAddress);

    }

}