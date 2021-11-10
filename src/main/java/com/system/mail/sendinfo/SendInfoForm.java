package com.system.mail.sendinfo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SendInfoForm {

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

    private Status status;

    @Length(max = 255, message = "제목은 최대 255자")
    private String subject;

    @NotNull
    @Length(max = 4000, message = "메일 본문은 최대 4000자")
    private String content;
}
