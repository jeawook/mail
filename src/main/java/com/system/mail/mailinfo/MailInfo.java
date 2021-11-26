package com.system.mail.mailinfo;

import com.system.mail.common.BaseTimeEntity;
import com.system.mail.common.MailAddress;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mysema.commons.lang.Assert.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class MailInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "mail_info_id")
    private Long id;

    @NotBlank
    @Length(max = 255,message = "설정명은 최대 255자")
    private String mailInfoName;

    @Embedded
    @Valid
    @NotNull
    @AttributeOverride(name = "email", column = @Column(name = "mailFrom"))
    @AttributeOverride(name = "name", column = @Column(name = "mailFromName"))
    private MailAddress mailFrom;

    @Embedded
    @Valid
    @NotNull
    @AttributeOverride(name = "email", column = @Column(name = "replyTo"))
    @AttributeOverride(name = "name", column = @Column(name = "replyToName"))
    private MailAddress replyTo;

    // 메일의 charset ex) utf-8
    @NotBlank
    private String charset;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ContentEncoding encoding;

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

    public String getHeaderContentType() {
        return contentType.getValue() + ";charset=\"" + charset + "\"";
    }

    public void updateByMailInfoForm(MailInfo mailInfo) {
        this.mailInfoName = mailInfo.getMailInfoName();
        this.charset = mailInfo.getCharset();
        this.contentType = mailInfo.getContentType();
        this.encoding = mailInfo.getEncoding();
        this.replyTo = mailInfo.getReplyTo();
        this.mailFrom = mailInfo.getMailFrom();
    }


    @Builder
    public MailInfo(@NotBlank @Length(max = 255, message = "설정명은 최대 255자") String mailInfoName, @NotNull @Valid MailAddress mailFrom,
                    @NotNull @Valid MailAddress replyTo, @NotBlank String charset, @NotNull ContentEncoding encoding, @NotNull ContentType contentType) {
        notNull(mailInfoName, "mailInfoName must not be null");
        notNull(mailFrom, "mailFrom must not be null");
        notNull(replyTo, "replyTo must not be null");
        notNull(charset, "charset must not be null");
        notNull(encoding, "encoding must not be null");
        notNull(contentType, "contentType must not be null");

        this.mailInfoName = mailInfoName;
        this.mailFrom = mailFrom;
        this.replyTo = replyTo;
        this.charset = charset;
        this.encoding = encoding;
        this.contentType = contentType;
    }
}
