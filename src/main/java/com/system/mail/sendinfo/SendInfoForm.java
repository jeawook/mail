package com.system.mail.sendinfo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyyMMddHHmm")
    private LocalDateTime sendDate;

    private LocalDateTime completedDate;

    private LocalDateTime createdDate;

    @NotNull
    private Status status;

    private String subject;

    private String content;
}
