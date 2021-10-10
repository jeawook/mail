package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private MailAddress mailAddress;

    private String macroValue;

    @ManyToOne
    @JoinColumn(name = "mail_group_id")
    private MailGroup mailGroup;

    public void setMailGroup(MailGroup mailGroup) {
        this.mailGroup = mailGroup;
    }

    public void setMailAddress(MailAddress mailAddress) {
        this.mailAddress = mailAddress;
    }

    public void setMacroValue(String macroValue) {
        this.macroValue = macroValue;
    }
}
