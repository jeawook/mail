package com.system.mail.api;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RecipientRequest {

    @Length(max = 20, message = "이름은 최대 20자")
    private String name;

    @NotBlank
    @Email
    private String email;

    // MailTemplate.macroKey 와 콤마 개수가 동일해야 하는 매크로 치환값
    private String macroValue;
}
