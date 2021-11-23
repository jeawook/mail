package com.system.mail.user;

import com.mysema.commons.lang.Assert;
import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Embedded
    @Valid
    private MailAddress mailAddress;

    private String macroValue;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "mail_group_id")
    private MailGroup mailGroup;

    @Builder
    public User(@Valid MailAddress mailAddress, String macroValue) {
        Assert.notNull(mailAddress,"mailAddress must not be null" );
        Assert.notNull(macroValue,"macroValue must not be null" );
        this.mailAddress = mailAddress;
        this.macroValue = macroValue;
    }

    public void setMailGroup(MailGroup mailGroup) {
        this.mailGroup = mailGroup;
    }


    public void setMacroValue(String macroValue) {
        this.macroValue = macroValue;
    }

}
