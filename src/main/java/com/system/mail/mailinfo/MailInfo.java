package com.system.mail.mailinfo;

import com.system.mail.common.BaseTimeEntity;
import com.system.mail.common.MailAddress;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "MailInfoBuilder")
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
    @NotNull
    @AttributeOverride(name = "email", column = @Column(name = "mailFrom"))
    @AttributeOverride(name = "name", column = @Column(name = "mailFromName"))
    private MailAddress mailFrom;

    @Embedded
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

    public void updateByMailInfoForm(MailInfoForm mailInfoForm) {
        this.mailInfoName = mailInfoForm.getMailInfoName();
        this.charset = mailInfoForm.getCharset();
        this.contentType = mailInfoForm.getContentType();
        this.encoding = mailInfoForm.getEncoding();
        this.replyTo = mailInfoForm.getReplyTo();
        this.mailFrom = mailInfoForm.getMailFrom();
    }

    public static MailInfoBuilder builder(String mailInfoName, ContentEncoding encoding, ContentType contentType, String charset, MailAddress replyTo, MailAddress mailFrom) {
        return MailInfoBuilder()
                .mailInfoName(mailInfoName)
                .encoding(encoding)
                .contentType(contentType)
                .charset(charset)
                .replyTo(replyTo)
                .mailFrom(mailFrom);
    }
}
