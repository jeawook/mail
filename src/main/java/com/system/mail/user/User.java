package com.system.mail.user;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder(builderMethodName = "UserBuilder")
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

    public static UserBuilder builder(MailAddress mailAddress, String macroValue) {
        return UserBuilder()
                .mailAddress(mailAddress)
                .macroValue(macroValue);
    }
    public boolean isChange(User user) {
        if (!this.macroValue.equals(user.getMacroValue()) || !this.mailAddress.equals(user.getMailAddress()) ) {
            return true;
        }
        return false;
    }
}
