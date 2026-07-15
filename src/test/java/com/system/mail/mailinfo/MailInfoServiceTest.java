package com.system.mail.mailinfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.system.mail.support.MailFixtures.mailAddress;
import static com.system.mail.support.MailFixtures.mailInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MailInfoServiceTest {

    @Autowired
    private MailInfoService mailInfoService;
    @Autowired
    private MailInfoRepository mailInfoRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void before() {
        // MailInfoController.init() 등 @PostConstruct 시딩 데이터가 있어도 카운트 기반 검증이
        // 흔들리지 않도록, 각 테스트가 스스로 만든 데이터만 존재하는 상태로 초기화한다.
        mailInfoRepository.deleteAll();
    }

    @Test
    @DisplayName("메일 정보 생성 테스트")
    void mailInfoServiceSaveTest () {
        MailInfo mailInfo = createMailInfo("테스트 메일");
        MailInfo saveMailInfo = mailInfoService.saveMailInfo(mailInfo);
        assertThat(mailInfo).isEqualTo(saveMailInfo);
        assertThat(mailInfo.getMailInfoName()).isEqualTo(saveMailInfo.getMailInfoName());
        assertThat(mailInfo.getMailFrom()).isEqualTo(saveMailInfo.getMailFrom());
        assertThat(mailInfo.getEncoding()).isEqualTo(saveMailInfo.getEncoding());
        assertThat(mailInfo.getContentType()).isEqualTo(saveMailInfo.getContentType());
        assertThat(mailInfo.getCharset()).isEqualTo(saveMailInfo.getCharset());
    }

    private MailInfo createMailInfo(String mailInfoName) {
        return mailInfo(mailAddress("no_reply", "test@email.com"), mailInfoName);
    }
    @Test
    @DisplayName("findMailInfoListByPage 테스트")
    void findMailInfoListByPageTest() {
        String mailInfoName = "테스트_";
        int pageSize = 10;
        for (int i = 0; i < 10; i++) {
            MailInfo mailInfo = createMailInfo(mailInfoName + i);
            mailInfoService.saveMailInfo(mailInfo);
        }
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Page<MailInfo> mailInfoByName = mailInfoService.findMailInfoListByPage(pageRequest);
        assertThat(mailInfoByName.getSize()).isEqualTo(pageSize);
        assertThat(mailInfoByName.getTotalPages()).isEqualTo(1);
        assertThat(mailInfoByName.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("mailInfo findMailInfoByName 테스트")
    void findMailInfoByNameTest() {
        String mailInfoName = "테스트_";
        int pageSize = 10;
        for (int i = 0; i < 10; i++) {
            MailInfo mailInfo = createMailInfo(mailInfoName + i);
            mailInfoService.saveMailInfo(mailInfo);
        }
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Page<MailInfo> mailInfoByName = mailInfoService.findMailInfoByName(mailInfoName, pageRequest);
        assertThat(mailInfoByName.getSize()).isEqualTo(pageSize);
        assertThat(mailInfoByName.getTotalPages()).isEqualTo(1);
        assertThat(mailInfoByName.getNumber()).isEqualTo(0);
    }
    @Test
    @DisplayName("updateMailInfo 테스트")
    void updateMailInfoTest() {
        String name = "update after";
        MailInfo mailInfo = createMailInfo("update before");
        MailInfo saveMailInfo = mailInfoService.saveMailInfo(mailInfo);
        MailInfo afterMailInfo = createMailInfo(name);
        mailInfoService.updateMailInfo(saveMailInfo.getId(), afterMailInfo);
        em.flush();
        em.clear();

        MailInfo mailInfoById = mailInfoService.findMailInfoById(saveMailInfo.getId());
        assertThat(mailInfoById.getMailInfoName()).isEqualTo(name);

    }

    @Test
    @DisplayName("updateMailInfo IllegalArgumentException 테스트")
    void updateMailInfoIllegalArgumentExceptionTest() {
        MailInfo mailInfo = createMailInfo("exception test");
        assertThatThrownBy(() -> mailInfoService.updateMailInfo(99L, mailInfo))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

}