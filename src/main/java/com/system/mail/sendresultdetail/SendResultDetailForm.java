package com.system.mail.sendresultdetail;

import com.system.mail.common.MailAddress;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SendResultDetailForm {

    private MailAddress mailAddress;

    private String macroValue;

    private String resultCode;

    private String resultMessage;

    private LocalDateTime completedDate;
}
