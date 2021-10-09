package com.system.mail.mailinfo;

import com.system.mail.common.BaseTimeEntity;
import com.system.mail.common.MailAddress;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "MailInfoBuilder")
@Getter @Setter
public class MailInfo extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "mail_info_id")
    private Long id;

    @NotBlank
    private String mailInfoName;

    @Embedded
    @NotNull
    @AttributeOverride(name = "email", column = @Column(name = "mailFrom"))
    @AttributeOverride(name = "name", column = @Column(name = "mailFromName"))
    private MailAddress mailFrom;

    @Embedded
    @NotNull
    @AttributeOverride(name = "email", column = @Column(name = "mailTo"))
    @AttributeOverride(name = "name", column = @Column(name = "mailToName"))
    private MailAddress mailTo;

    @Embedded
    @NotNull
    @AttributeOverride(name = "email", column = @Column(name = "replyTo"))
    @AttributeOverride(name = "name", column = @Column(name = "replyToName"))
    private MailAddress replyTo;


    // 메일의 charset ex) utf-8
    @NotBlank
    private String charset;

    // 메일 본문의 타입 ex) text/html or text/plain ....
    @Enumerated(EnumType.STRING)
    @NotNull
    private ContentType contentType;

    public String getHeaderFrom() {
        return mailFrom.getHeaderAddress();
    }
    public String getHeaderReply() {
        return replyTo.getHeaderAddress();
    }
    public String getHeaderTo() {
        return mailTo.getHeaderAddress();
    }

    public static MailInfoBuilder builder(MailInfoForm mailInfoForm) {
        return MailInfoBuilder()
                .mailInfoName(mailInfoForm.getMailInfoName())
                .mailFrom(mailInfoForm.getMailFrom())
                .mailTo(mailInfoForm.getMailTo())
                .replyTo(mailInfoForm.getReplyTo())
                .charset(mailInfoForm.getCharset())
                .contentType(mailInfoForm.getContentType());
    }
}
