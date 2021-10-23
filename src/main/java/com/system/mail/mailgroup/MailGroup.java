package com.system.mail.mailgroup;

import com.system.mail.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "MailGroupBuilder")
@Getter @Setter
public class MailGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue
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

    public int getUserCnt() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public static MailGroupBuilder builder(String mailGroupName, String macroKey) {
        return MailGroupBuilder()
                .mailGroupName(mailGroupName)
                .macroKey(macroKey);
    }
}


