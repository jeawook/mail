package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import lombok.Builder;

import javax.persistence.*;

@Entity
@Builder
public class MailList {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private MailAddress mailAddress;

    private String macroValue;

    @ManyToOne
    @JoinColumn(name = "mail_group_id")
    private MailGroup mailGroup;
}
