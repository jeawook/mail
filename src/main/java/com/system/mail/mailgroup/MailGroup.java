package com.system.mail.mailgroup;

import com.system.mail.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class MailGroup extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "mail_group_id")
    private Long id;

    private String mailGroupName;

    private String macroKey;

    @OneToMany(mappedBy = "mailGroup", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        this.users.add(user);
        user.setMailGroup(this);
    }

    public void setUsers(ArrayList<User> users) {
        users.forEach(this::addUser);
    }
}
