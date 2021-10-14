package com.system.mail.sendinfo;

import com.system.mail.common.BaseTimeEntity;
import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.sendresult.SendResult;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "SendInfoBuilder")
@Getter
public class SendInfo extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "send_info_id")
    private Long id;

    @NotNull
    private LocalDateTime sendDate;

    private LocalDateTime completedDate;

    private String subject;

    private String content;

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


    private void setMailInfo(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }
    private void setMailGroup(MailGroup mailGroup) {
        this.mailGroup = mailGroup;
    }
    private void setComplete() {
        completedDate = LocalDateTime.now();
    }

    public void mailStatusSending() {
        this.status = Status.SENDING;
    }
    public void mailStatusEnd() {
        this.status = Status.COMPLETE;
    }
    public void mailStatusWait() {
        this.status = Status.WAIT;
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

    public static SendInfoBuilder SendInfo(SendInfoForm sendInfoForm, MailInfo mailInfo, MailGroup mailGroup) {
        return SendInfoBuilder().mailInfo(mailInfo)
                .mailGroup(mailGroup)
                .sendDate(sendInfoForm.getSendDate())
                .subject(sendInfoForm.getSubject())
                .content(sendInfoForm.getContent())
                .status(Status.REGISTER);
    }
}
