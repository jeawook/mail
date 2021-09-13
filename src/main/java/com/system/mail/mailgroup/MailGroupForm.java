package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class MailGroupForm {

    @NotBlank
    private String name;

    private String macroKey;

    @NotNull
    private ArrayList<MailAddress> mailAddress;

    private String macroValue;

}
