package com.system.mail.mailgroup;

import com.system.mail.MailAddress;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class MailGroupForm {

    @NotBlank
    private String name;

    @NotNull
    private MailAddress mailAddress;

    private String macroKey;

    private String macroValue;

}
