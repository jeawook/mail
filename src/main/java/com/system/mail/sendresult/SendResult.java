package com.system.mail.sendresult;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.user.User;
import com.system.mail.sendresultdetail.SendResultDetail;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendResult {

    @Id @GeneratedValue
    @Column(name = "send_result_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "send_info_id")
    private SendInfo sendInfo;

    private String macroKey;

    private int totalCnt = 0;

    private int completedCnt = 0;

    private int errorCnt = 0;

    private int successCnt = 0;

    @OneToMany(mappedBy = "sendResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<SendResultDetail> sendResultDetails = new ArrayList<>();

    @Builder
    public SendResult(SendInfo sendInfo) {
        setSendInfo(sendInfo);
        this.macroKey = sendInfo.getMacroKey();
        this.totalCnt = sendInfo.getMailGroup().getUserCnt();
        createSendResultDetails(sendInfo.getMailGroup().getUsers());
    }


    private void setSendInfo(SendInfo sendInfo) {
        this.sendInfo = sendInfo;
        sendInfo.setSendResult(this);
    }

    private void addCompleteCnt() {
        this.completedCnt++;
    }

    public void addSendResultDetail(SendResultDetail sendResultDetail) {
        this.sendResultDetails.add(sendResultDetail);
        sendResultDetail.setSendResult(this);
    }

    private void createSendResultDetails(List<User> users) {
        users.forEach(user -> addSendResultDetail(SendResultDetail.builder()
                .mailAddress(user.getMailAddress())
                .macroValue(user.getMacroValue()).build()));
    }

    private void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }
    private void addSuccessCnt() {
        this.successCnt++;
        addCompletedCnt();
    }
    private void addErrorCnt() {
        this.errorCnt++;
        addCompletedCnt();
    }
    private void addCompletedCnt() {
        this.completedCnt++;
    }

    public void checkResultAddCnt(String resultCode) {
        if (resultCode.equals("250")) {
            addSuccessCnt();
            return;
        }
        addErrorCnt();

    }

}
