package com.system.mail.mailgroup;

import com.mysema.commons.lang.Assert;
import com.system.mail.common.BaseTimeEntity;
import com.system.mail.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.mysema.commons.lang.Assert.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class MailGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "mail_group_id")
    private Long id;

    private String mailGroupName;

    private String macroKey;

    @OneToMany(mappedBy = "mailGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        this.users.add(user);
        user.setMailGroup(this);
    }

    public int getUserCnt() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    @Builder
    public MailGroup(String mailGroupName, String macroKey) {
        notNull(mailGroupName, "mailGroupName must not be null");
        notNull(macroKey, "macroKey must not be null");
        this.mailGroupName = mailGroupName;
        this.macroKey = macroKey;
    }
}


