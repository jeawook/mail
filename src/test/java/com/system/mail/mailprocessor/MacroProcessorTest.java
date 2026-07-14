package com.system.mail.mailprocessor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MacroProcessorTest {
    @Autowired
    private MacroProcessor macroProcessor;

    @Test
    void macroProcessorTest() {
        String content = "[$name$]님 안녕하세요, [$email$], [$test$], [$test1$]";
        String macroKey = "name,email,test";
        String macroValue = "재욱,pdj13579@nate.com,test";
        String result = macroProcessor.process(macroKey, macroValue, content);
        assertThat(result).isEqualTo("재욱님 안녕하세요, pdj13579@nate.com, test, [$test1$]");
    }

    @Test
    void macroProcessorBlankTest() {
        String content = "";
        String macroKey = "name,email,test";
        String macroValue = ",,";
        String result = macroProcessor.process(macroKey, macroValue, content);
        assertThat(result).isEqualTo("");
    }

    @Test
    void extractKeysReturnsDistinctKeysInOrderOfAppearance() {
        String target = "[$name$]님 안녕하세요, [$email$], [$name$] 다시 등장";
        assertThat(macroProcessor.extractKeys(target)).containsExactly("name", "email");
    }

    @Test
    void extractKeysReturnsEmptySetWhenNoPlaceholder() {
        assertThat(macroProcessor.extractKeys("플레이스홀더가 없는 문장")).isEmpty();
        assertThat(macroProcessor.extractKeys(null)).isEmpty();
    }

}