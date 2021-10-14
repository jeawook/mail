package com.system.mail.sendresultdetail;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.User;
import com.system.mail.mailprocessor.SMTPResult;
import com.system.mail.sendresult.SendResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "SendResultDetailBuilder")
public class SendResultDetail {

    @Id @GeneratedValue
    @Column(name = "send_result_detail_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private MailAddress mailAddress;

    private String resultCode;

    private String resultMessage;

    private LocalDateTime completedDate;

    @ManyToOne
    @JoinColumn(name = "send_result_id")
    private SendResult sendResult;

    public User getUser() {
        return user;
    }
    public String getDomain() {
        return user.getMailAddress().getDomain();
    }
    public Long getId() {
        return id;
    }
    public MailAddress getMailAddress() {
        return user.getMailAddress();
    }

    public void setSendResult(SendResult sendResult) {
        this.sendResult = sendResult;
    }

    public String getMacroValue() {
        return user.getMacroValue();
    }
    public void setCompleted() {
        completedDate = LocalDateTime.now();
    }
    public void setResult(SMTPResult smtpResult) {
        this.resultCode = smtpResult.getResultCode();
        this.resultMessage = smtpResult.getResultMessage();
        setCompleted();
    }
    public static SendResultDetailBuilder SendResultDetail(User user) {
        return SendResultDetailBuilder().user(user).mailAddress(user.getMailAddress());
    }
}
