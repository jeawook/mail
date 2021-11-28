package com.system.mail.sendresult;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResultInfoDto {
    private Long sendResultId;

    private int totalCnt;

    private int errorCnt;

    private int successCnt;

    private int completedCnt;

    private String macroKey;

    private LocalDateTime sendDate;

    private LocalDateTime completedDate;

    @Builder
    @QueryProjection
    public ResultInfoDto(Long sendResultId, int totalCnt, int errorCnt,
                         int successCnt, int completedCnt, String macroKey, LocalDateTime sendDate, LocalDateTime completedDate) {
        this.sendResultId = sendResultId;
        this.totalCnt = totalCnt;
        this.errorCnt = errorCnt;
        this.successCnt = successCnt;
        this.completedCnt = completedCnt;
        this.macroKey = macroKey;
        this.sendDate = sendDate;
        this.completedDate = completedDate;
    }
}
