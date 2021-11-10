package com.system.mail.mailinfo;


import com.system.mail.common.MailAddress;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailInfoForm {

    @NotBlank
    @Length(max = 255,message = "설정명은 최대 255자")
    private String mailInfoName;

    @NotBlank
    private String charset;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ContentType contentType;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ContentEncoding encoding;

    @Embedded
    @NotNull(message = "mailFrom 의 주소는 필수입니다.")
    @Valid
    private MailAddress mailFrom;

    @Embedded
    @NotNull(message = "replyTo의 주소는 필수 로 입력 되어야 합니다.")
    @Valid
    private MailAddress replyTo;

}
