package com.system.mail.user;

import com.system.mail.common.MailAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserForm {

    private Long id;
    @NotNull
    @Valid
    private MailAddress mailAddress;

    @Length(max = 4000, message = "최대 4000자 까지 가능 합니다.")
    private String macroValue;

}
