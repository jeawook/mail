package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import lombok.Data;

@Data
public class MailDTO {

    private String helo;

    private MailAddress mailAddress;

    private String mailFrom;

    private String rcpTo;

    private String data;

    public String getDomain() {
        return mailAddress.getDomain();
    }
}
