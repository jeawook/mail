package com.system.mail.api;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MailSendRequest {

    @NotNull
    private Long templateId;

    @NotNull
    private Long mailInfoId;

    // 미지정 시 즉시 발송(now)
    private LocalDateTime sendDate;

    @NotEmpty
    @Valid
    private List<RecipientRequest> recipients;
}
