package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MailDtoTest {

    @Test
    void createTest() {
        String data = "data";
        String rcpToName = "수신자";
        String mailFromName = "발신자";
        String rcpToEmail = "pdj13579@rcpto.ttt";
        String mailFromEmail = "pdj13579@mailFrom.ttt";
        MailAddress rcpTo = MailAddress.builder().name(rcpToName).email(rcpToEmail).build();
        MailAddress mailFrom = MailAddress.builder().name(mailFromName).email(mailFromEmail).build();
        MailDto mailDto = MailDto.builder().rcpTo(rcpTo).mailFrom(mailFrom).data(data).build();

        assertThat(mailDto.getData()).isEqualTo(data);
        assertThat(mailDto.getMailFrom()).isEqualTo(mailFrom);
        assertThat(mailDto.getRcpTo()).isEqualTo(rcpTo);
        assertThat(mailDto.getRcpToAddress()).isEqualTo(rcpTo.getAddress());
        assertThat(mailDto.getRcpToDomain()).isEqualTo(rcpTo.getDomain());

    }

    @Test
    void createIllegalArgumentExceptionTest() {
        String data = "data";
        String rcpToName = "수신자";
        String mailFromName = "발신자";
        String rcpToEmail = "pdj13579@rcpto.ttt";
        String mailFromEmail = "pdj13579@mailFrom.ttt";
        MailAddress rcpTo = MailAddress.builder().name(rcpToName).email(rcpToEmail).build();
        MailAddress mailFrom = MailAddress.builder().name(mailFromName).email(mailFromEmail).build();

        assertThatThrownBy(() -> MailDto.builder()
                .rcpTo(rcpTo)
                .mailFrom(mailFrom)
                .data(null)
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("data must not be null");

        assertThatThrownBy(() -> MailDto.builder()
                .rcpTo(null)
                .mailFrom(mailFrom)
                .data(data)
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("rcpTo must not be null");

        assertThatThrownBy(() -> MailDto.builder()
                .rcpTo(rcpTo)
                .mailFrom(null)
                .data(data)
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("mailFrom must not be null");
    }

}