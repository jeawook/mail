package com.system.mail.sendinfo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SendInfoForm {

    @NotNull
    private Long sendInfoId;

    @NotNull
    private Long mailGroupId;

    @NotNull
    private Long mailInfoId;

    @NotNull
    private LocalDateTime sendDate;

    private LocalDateTime completedDate;

    private LocalDateTime createdDate;

    @NotNull
    private Status status;

    private String subject;

    private String content;
}
