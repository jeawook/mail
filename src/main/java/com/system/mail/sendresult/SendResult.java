package com.system.mail.sendresult;

import com.system.mail.common.MailAddress;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
public class SendResult {

    @Id @GeneratedValue
    @Column(name = "send_result_id")
    private Long id;

    @NotNull
    private MailAddress mailAddress;

    @NotNull
    private Status status;

    @NotNull
    private LocalDateTime sendDate;

    @NotNull
    private LocalDateTime completedDate;

    @NotNull
    @Min(1)
    private Long totalCnt;

    @NotNull
    @Min(0)
    private Long completedCnt;

}
