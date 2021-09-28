package com.system.mail.sendinfo;

import com.system.mail.common.BaseTimeEntity;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.MailInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    private void updateStatus(Status status) {
        this.status = status;
    }
    private void setMailInfo(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }
    private void setMailGroup(MailGroup mailGroup) {
        this.mailGroup = mailGroup;
    }
    private void setCompletedDate() {
        completedDate = LocalDateTime.now();
    }
}
