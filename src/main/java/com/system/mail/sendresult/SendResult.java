package com.system.mail.sendresult;

import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendresultdetail.SendResultDetail;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<SendResultDetail> details = resolveRecipients(sendInfo);
        this.totalCnt = details.size();
        details.forEach(this::addSendResultDetail);
    }

    // mailGroup 이 등록된 발송(UI)은 mailGroup.users, 없는 단발성 발송(API)은 sendInfo.recipients 를 사용
    private List<SendResultDetail> resolveRecipients(SendInfo sendInfo) {
        if (sendInfo.getMailGroup() != null) {
            return sendInfo.getMailGroup().getUsers().stream()
                    .map(user -> SendResultDetail.builder()
                            .mailAddress(user.getMailAddress())
                            .macroValue(user.getMacroValue())
                            .build())
                    .collect(Collectors.toList());
        }
        return sendInfo.getRecipients().stream()
                .map(recipient -> SendResultDetail.builder()
                        .mailAddress(recipient.getMailAddress())
                        .macroValue(recipient.getMacroValue())
                        .build())
                .collect(Collectors.toList());
    }


    public void setSendInfo(SendInfo sendInfo) {
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
