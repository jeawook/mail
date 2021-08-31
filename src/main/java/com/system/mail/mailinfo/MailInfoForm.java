package com.system.mail.mailinfo;


import com.system.mail.common.MailAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
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
    private MailAddress mailFrom;

    @NotNull
    private MailAddress mailTo;

    @NotNull
    private MailAddress replyTo;

}
