package com.system.mail.sendresult;

import com.system.mail.common.MailAddress;
import com.system.mail.sendinfo.Status;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Builder
@Getter
public class SendResult {

    @Id @GeneratedValue
    @Column(name = "send_result_id")
    private Long id;
    @NotNull
    private Status status;

    @NotNull
    @Min(1)
    private Long totalCnt;

    @NotNull
    @Min(0)
    private Long completedCnt;

    private ArrayList<SendResultDetail> sendResultDetails = new ArrayList<>();

}
