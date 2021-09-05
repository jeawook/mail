package com.system.mail.mailinfo;

import com.system.mail.common.MailAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class MailInfoTest {

    @DisplayName("mailInfo생성테스트")
    @Test
    void createMailInfoTest() {

        HashMap<String, String> macro = new HashMap<>();
        macro.put("name", "박재욱");

    }

}