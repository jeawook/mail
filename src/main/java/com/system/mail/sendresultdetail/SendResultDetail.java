package com.system.mail.sendresultdetail;

import com.system.mail.common.MailAddress;
import com.system.mail.user.User;
import com.system.mail.mailprocessor.SMTPResult;
import com.system.mail.sendresult.SendResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "SendResultDetailBuilder")
public class SendResultDetail {

    @Id @GeneratedValue
    @Column(name = "send_result_detail_id")
    private Long id;

    private MailAddress mailAddress;

    private String macroValue;

    private String resultCode;

    @Column(length = 2000)
    private String resultMessage;

    private LocalDateTime completedDate;

    @ManyToOne
    @JoinColumn(name = "send_result_id")
    private SendResult sendResult;

    public String getDomain() {
        return this.mailAddress.getDomain();
    }
    public Long getId() {
        return id;
    }
    public MailAddress getMailAddress() {
        return this.mailAddress;
    }

    public void setSendResult(SendResult sendResult) {
        this.sendResult = sendResult;
    }

    public String getResultCode() {
        return this.resultCode;
    }
    public String getResultMessage() {
        return  this.resultMessage;
    }
    public LocalDateTime getCompletedDate() {
        return this.completedDate;
    }
    public String getMacroValue() {
        return this.macroValue;
    }
    private void setCompleted() {
        completedDate = LocalDateTime.now();
    }
    public void setResult(SMTPResult smtpResult) {
        this.resultCode = smtpResult.getResultCode();
        this.resultMessage = smtpResult.getResultMessage();
        sendResult.checkResultAddCnt(resultCode);
        setCompleted();
    }

    public static SendResultDetailBuilder builder(User user) {
        return SendResultDetailBuilder()
                .mailAddress(user.getMailAddress())
                .macroValue(user.getMacroValue());
    }
}
