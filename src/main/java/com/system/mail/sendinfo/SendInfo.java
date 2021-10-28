package com.system.mail.sendinfo;

import com.system.mail.common.BaseTimeEntity;
import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.sendresult.SendResult;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "SendInfoBuilder")
@Getter @Setter
public class SendInfo extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "send_info_id")
    private Long id;

    @NotNull
    private LocalDateTime sendDate;

    private LocalDateTime completedDate;

    @NotNull
    private String subject;

    @NotNull
    @Column(length = 4000)
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @ManyToOne
    @JoinColumn(name = "mail_info_id")
    private MailInfo mailInfo;

    @ManyToOne
    @JoinColumn(name = "mail_group_id")
    private MailGroup mailGroup;

    @OneToOne
    @JoinColumn(name = "send_result_id")
    private SendResult sendResult;


    public void setMailInfo(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }
    public void setMailGroup(MailGroup mailGroup) {
        this.mailGroup = mailGroup;
    }
    private void setCompletedDate() {
        completedDate = LocalDateTime.now();
    }
    public void mailRegister() {
        this.status = Status.REGISTER;
    }
    public void mailStatusSending() {
        this.status = Status.SENDING;
    }
    public void mailStatusComplete() {
        this.status = Status.COMPLETE;
        setCompletedDate();
    }

    public MailAddress getMailFrom() {
        return mailInfo.getMailFrom();
    }
    public void setSendResult(SendResult sendResult) {
        this.sendResult = sendResult;
    }
    public String getMacroKey() {
        return mailGroup.getMacroKey();
    }

    public static SendInfoBuilder builder(String subject, String content, LocalDateTime sendDate, Status status) {
        return SendInfoBuilder().subject(subject)
                .content(content)
                .sendDate(sendDate)
                .status(status);
    }
}
