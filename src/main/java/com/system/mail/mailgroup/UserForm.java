package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserForm {

    private Long id;
    @NotNull
    private MailAddress mailAddress;
    @NotBlank
    private String macroValue;

}
