package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
