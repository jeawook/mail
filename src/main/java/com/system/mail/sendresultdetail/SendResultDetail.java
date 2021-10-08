package com.system.mail.sendresultdetail;

import com.system.mail.common.MailAddress;
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

    @NotNull
    private MailAddress mailAddress;

    private String resultCode;

    private String resultMessage;

    private LocalDateTime completedDate;

    @ManyToOne
    @JoinColumn(name = "send_result_id")
    private SendResult sendResult;

    public void setSendResult(SendResult sendResult) {
        this.sendResult = sendResult;
    }
    public void setCompleted() {
        completedDate = LocalDateTime.now();
    }
    public void setResult(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        setCompleted();
    }
    public static SendResultDetailBuilder SendResultDetail(MailAddress mailAddress) {
        return SendResultDetailBuilder().mailAddress(mailAddress);
    }
}
