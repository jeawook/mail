package com.system.mail.sendresultdetail;

import com.system.mail.common.MailAddress;
import com.system.mail.user.UserForm;
import lombok.Builder;
import lombok.Data;

@Data
public class SendResultDetailForm {

    private UserForm user;

    private MailAddress mailAddress;

    private String resultCode;

    private String resultMessage;

}
