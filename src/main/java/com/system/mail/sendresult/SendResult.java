package com.system.mail.sendresult;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.User;
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
@Builder
public class SendResult {

    @Id @GeneratedValue
    @Column(name = "send_result_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "mail_group_id")
    private MailGroup mailGroup;

    @Min(1)
    private int totalCnt;

    @Min(0)
    private int completedCnt;

    @OneToMany(mappedBy = "sendResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<SendResultDetail> sendResultDetails = new ArrayList<>();

    private void addCompleteCnt() {
        this.completedCnt++;
    }

    public void addSendResultDetail(SendResultDetail sendResultDetail) {
        this.sendResultDetails.add(sendResultDetail);
        sendResultDetail.setSendResult(this);
    }

    public void createSendResultDetails(List<User> users) {
        users.forEach(user -> addSendResultDetail(SendResultDetail.builder().user(user).build()));
    }

}
