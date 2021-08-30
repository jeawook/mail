package com.system.mail.mailinfo;

import com.system.mail.common.MailAddress;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Getter
public class MailInfo {

    @Id @GeneratedValue
    @Column(name = "mail_info_id")
    private Long id;

    @NotBlank
    private String name;

    @Embedded
    @NotNull
    @AttributeOverride(name = "address", column = @Column(name = "mailFrom"))
    @AttributeOverride(name = "name", column = @Column(name = "mailFromName"))
    private MailAddress mailFrom;

    @Embedded
    @NotNull
    @AttributeOverride(name = "address", column = @Column(name = "mailTo"))
    @AttributeOverride(name = "name", column = @Column(name = "mailToName"))
    private MailAddress mailTo;

    @Embedded
    @NotNull
    @AttributeOverride(name = "address", column = @Column(name = "replyTo"))
    @AttributeOverride(name = "name", column = @Column(name = "replyToName"))
    private MailAddress replyTo;

    // 메일 본문
    @NotBlank
    private String content;

    // 메일의 charset ex) utf-8
    @NotBlank
    private String charset;

    // 메일 본문의 타입 ex) text/html or text/plain ....
    @NotBlank
    private String contentType;


}
