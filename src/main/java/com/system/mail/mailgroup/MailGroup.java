package com.system.mail.mailgroup;

import com.system.mail.MailAddress;
import com.system.mail.sendinfo.SendInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MailGroup {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private MailAddress mailAddress;

    @MapKeyColumn(name = "macroKey")
    @Column(name = "macroValue")
    private Map<String, String> macro = new HashMap<>();

    @OneToMany(mappedBy = "sendInfo", fetch = FetchType.LAZY)
    private List<SendInfo> sendInfoList = new ArrayList<>();


    @Builder
    public MailGroup(@NotBlank String name,@NotNull MailAddress mailAddress, Map<String, String> macro) {
        this.name = name;
        this.mailAddress = mailAddress;
        this.macro = macro;
    }
}
