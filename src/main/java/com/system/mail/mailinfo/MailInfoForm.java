package com.system.mail.mailinfo;


import com.system.mail.common.MailAddress;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MailInfoForm {

    @NotBlank
    private String mailInfoName;
    @NotBlank
    private String charset;
    @NotBlank
    private String contentType;
    @NotBlank
    private String content;

    @NotNull
    private MailAddress mailForm;

    @NotNull
    private MailAddress mailTo;

    @NotNull
    private MailAddress replyTo;

}
