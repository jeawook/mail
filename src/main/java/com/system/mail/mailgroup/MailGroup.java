package com.system.mail.mailgroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MailGroup {

    @Id @GeneratedValue
    @Column(name = "mail_group_id")
    private Long id;

    private String name;

    private String macroKey;

    @OneToMany(mappedBy = "mailGroup")
    private List<MailList> mailLists = new ArrayList<>();

}
