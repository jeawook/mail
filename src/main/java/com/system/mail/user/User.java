package com.system.mail.user;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "UserBuilder")
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private MailAddress mailAddress;

    private String macroValue;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "mail_group_id")
    private MailGroup mailGroup;

    public void setMailGroup(MailGroup mailGroup) {
        this.mailGroup = mailGroup;
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
