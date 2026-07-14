package com.system.mail.sendinfo;

import com.mysema.commons.lang.Assert;
import com.system.mail.common.BaseTimeEntity;
import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.sendresult.SendResult;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mysema.commons.lang.Assert.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@ToString
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
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @ManyToOne
    @JoinColumn(name = "mail_info_id")
    @NotNull
    private MailInfo mailInfo;

    // MailGroup 을 사전 등록하지 않는 API 단발성 발송에서는 null 일 수 있음
    @ManyToOne
    @JoinColumn(name = "mail_group_id")
    private MailGroup mailGroup;

    // mailGroup 이 있으면 mailGroup.macroKey 를 등록 시점에 복사, 없으면(API 단발성 발송) 직접 전달된 값
    private String macroKey;

    @OneToMany(mappedBy = "sendInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SendRecipient> recipients = new ArrayList<>();

    @Builder
    public SendInfo(@NotNull LocalDateTime sendDate,@NotNull MailInfo mailInfo, MailGroup mailGroup,
                    @NotNull String subject, @NotNull String content, @NotNull Status status, String macroKey) {
        notNull(sendDate, "sendDate must not be null");
        notNull(subject, "subject must not be null");
        notNull(content, "content must not be null");
        notNull(status, "status must not be null");
        notNull(mailInfo, "mailInfo must not be null");

        this.mailGroup = mailGroup;
        this.mailInfo = mailInfo;
        this.sendDate = sendDate;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.macroKey = mailGroup != null ? mailGroup.getMacroKey() : macroKey;
    }

    public void addRecipient(SendRecipient recipient) {
        this.recipients.add(recipient);
        recipient.setSendInfo(this);
    }


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
}
