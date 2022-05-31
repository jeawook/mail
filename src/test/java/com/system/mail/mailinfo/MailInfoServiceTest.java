package com.system.mail.mailinfo;

import com.system.mail.common.MailAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MailInfoServiceTest {

    @Autowired
    private MailInfoService mailInfoService;
    @Autowired
    private EntityManager em;

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
        MailAddress mail = MailAddress.builder().name("no_reply").email("test@email.com").build();
        MailInfo mailInfo = MailInfo.builder()
                .mailFrom(mail)
                .replyTo(mail)
                .charset("utf-8")
                .encoding(ContentEncoding.BASE64)
                .contentType(ContentType.HTML)
                .mailInfoName(mailInfoName)
                .build();
        return mailInfo;
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
        assertThat(mailInfoByName.getTotalPages()).isEqualTo(2);
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
        assertThat(mailInfoByName.getTotalPages()).isEqualTo(2);
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