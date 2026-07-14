package com.system.mail.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailSendResponse {
    private final Long sendInfoId;
    private final String status;
}
