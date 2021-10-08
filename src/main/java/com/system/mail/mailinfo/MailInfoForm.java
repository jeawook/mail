package com.system.mail.mailinfo;


import com.system.mail.common.MailAddress;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailInfoForm {

    @NotBlank
    private String mailInfoName;

    @NotBlank
    private String charset;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ContentType contentType;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @Embedded
    @NotNull(message = "mailFrom의 주소는 필수입니다.")
    private MailAddress mailFrom;

    @Embedded
    @NotNull(message = "mailTo의 주소는 필수 로 입력 되어야 합니다.")
    private MailAddress mailTo;

    @Embedded
    @NotNull(message = "replyTo의 주소는 필수 로 입력 되어야 합니다.")
    private MailAddress replyTo;

}
