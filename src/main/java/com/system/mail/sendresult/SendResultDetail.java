package com.system.mail.sendresult;

import com.system.mail.common.MailAddress;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SendResultDetail {

    @Id @GeneratedValue
    @Column(name = "send_result_detail_id")
    private Long id;

    @NotNull
    private MailAddress mailAddress;

    private String resultCode;

    private String resultMessage;

    private String completedDate;

    @ManyToOne
    @JoinColumn(name = "send_result_id")
    private SendResult sendResult;

}
