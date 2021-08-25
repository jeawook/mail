package com.system.mail.mailgroup;

import com.system.mail.MailAddress;
import com.system.mail.sendinfo.SendInfo;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Builder
public class MailGroup {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @NotNull
    private MailAddress mailAddress;

    @MapKeyColumn(name = "macroKey")
    @Column(name = "macroValue")
    private Map<String, String> macro = new HashMap<>();

    @OneToMany(mappedBy = "sendInfo", fetch = FetchType.LAZY)
    private List<SendInfo> sendInfoList = new ArrayList<>();

}
