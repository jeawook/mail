package com.system.mail.sendinfo;

import com.system.mail.common.MailAddress;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.mysema.commons.lang.Assert.notNull;

/**
 * MailGroup 을 사전 등록하지 않는 API 단발성 발송에서 사용되는 SendInfo 전용 수신자.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class SendRecipient {

    @Id @GeneratedValue
    @Column(name = "send_recipient_id")
    private Long id;

    @Embedded
    private MailAddress mailAddress;

    private String macroValue;

    @ManyToOne
    @JoinColumn(name = "send_info_id")
    private SendInfo sendInfo;

    @Builder
    public SendRecipient(MailAddress mailAddress, String macroValue) {
        notNull(mailAddress, "mailAddress must not be null");
        notNull(macroValue, "macroValue must not be null");
        this.mailAddress = mailAddress;
        this.macroValue = macroValue;
    }

    public void setSendInfo(SendInfo sendInfo) {
        this.sendInfo = sendInfo;
    }
}
