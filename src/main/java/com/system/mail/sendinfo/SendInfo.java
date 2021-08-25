package com.system.mail.sendinfo;

import com.system.mail.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder
public class SendInfo {

    @Id @GeneratedValue
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

    @ManyToOne
    @NotNull
    private MailGroup mailGroup;

}
