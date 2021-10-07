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
import java.util.List;

@Entity
@Getter
public class SendResult {

    @Id @GeneratedValue
    @Column(name = "send_result_id")
    private Long id;

    private Status status;

    @Min(1)
    private Long totalCnt;

    @Min(0)
    private Long completedCnt;

    @OneToMany(mappedBy = "sendResult")
    private List<SendResultDetail> sendResultDetails = new ArrayList<>();

}
