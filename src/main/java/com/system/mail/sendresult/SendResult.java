package com.system.mail.sendresult;

import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.Status;
import com.system.mail.sendresultdetail.SendResultDetail;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "SendResultBuilder")
public class SendResult {

    @Id @GeneratedValue
    @Column(name = "send_result_id")
    private Long id;

    @Min(1)
    private int totalCnt;

    @Min(0)
    private int completedCnt;

    @OneToMany(mappedBy = "sendResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SendResultDetail> sendResultDetails = new ArrayList<>();

    private void addCompleteCnt() {
        this.completedCnt++;
    }

    public void addSendResultDetail(SendResultDetail sendResultDetail) {
        this.sendResultDetails.add(sendResultDetail);
        sendResultDetail.setSendResult(this);
    }

    public void addSendResultDetails(List<SendResultDetail> sendResultDetails) {
        sendResultDetails.forEach(this::addSendResultDetail);
    }


    public static SendResultBuilder SendResult(int totalCnt) {
        return SendResultBuilder().totalCnt(totalCnt).completedCnt(0);
    }


}
