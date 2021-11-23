package com.system.mail.mailgroup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MailGroupRepositoryTest {

    @Autowired
    MailGroupRepository mailGroupRepository;

    @BeforeEach
    void before() {
        for (int i = 0; i < 100; i++) {
            mailGroupRepository.save(MailGroup.builder().mailGroupName("name_"+i).macroKey("").build());
        }
    }
    @Test
    void searchTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MailGroup> result = mailGroupRepository.searchByName("name", pageRequest);

        assertThat(result.getTotalPages()).isEqualTo(10);
        assertThat(result.getSize()).isEqualTo(10);
    }
    @Test
    void searchNameNullTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MailGroup> result = mailGroupRepository.searchByName(null, pageRequest);

        assertThat(result.getTotalPages()).isEqualTo(10);
        assertThat(result.getSize()).isEqualTo(10);
    }

}