package com.system.mail.sendinfo;

import com.system.mail.common.BaseTimeEntity;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.MailInfo;
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


    private void setMailInfo(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }
    private void setMailGroup(MailGroup mailGroup) {
        this.mailGroup = mailGroup;
    }
    private void setCompletedDate() {
        completedDate = LocalDateTime.now();
    }

    private void mailSending() {
        this.status = Status.SENDING;
    }
    private void mailEnd() {
        this.status = Status.COMPLETE;
    }
    private void mailWait() {
        this.status = Status.WAIT;
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
