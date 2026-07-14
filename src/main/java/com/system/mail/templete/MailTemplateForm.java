package com.system.mail.templete;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailTemplateForm {

    @NotBlank
    @Length(max = 255, message = "템플릿명은 최대 255자")
    private String templateName;

    @Length(max = 255, message = "제목은 최대 255자")
    private String subject;

    @NotBlank
    @Length(max = 4000, message = "메일 본문은 최대 4000자")
    private String content;

    private String macroKey;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
