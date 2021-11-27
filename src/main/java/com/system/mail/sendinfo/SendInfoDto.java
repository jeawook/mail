package com.system.mail.sendinfo;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SendInfoDto {

    private Long id;

    private LocalDateTime sendDate;

    private LocalDateTime completedDate;

    private String mailInfoName;

    private String mailGroupName;

    private Status status;

    private String subject;

    private String content;

    private Long sendResultId;

    @Builder
    @QueryProjection
    public SendInfoDto(Long id, LocalDateTime sendDate, LocalDateTime completedDate, String mailInfoName, String mailGroupName, Status status, String subject, String content, Long sendResultId) {
        this.id = id;
        this.sendDate = sendDate;
        this.completedDate = completedDate;
        this.mailInfoName = mailInfoName;
        this.mailGroupName = mailGroupName;
        this.status = status;
        this.subject = subject;
        this.content = content;
        this.sendResultId = sendResultId;
    }
}
