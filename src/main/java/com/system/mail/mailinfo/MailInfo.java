package com.system.mail.mailinfo;

import com.system.mail.common.BaseTimeEntity;
import com.system.mail.common.MailAddress;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    @AttributeOverride(name = "email", column = @Column(name = "replyTo"))
    @AttributeOverride(name = "name", column = @Column(name = "replyToName"))
    private MailAddress replyTo;


    // 메일의 charset ex) utf-8
    @NotBlank
    private String charset;

    private String encoding;

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
        return contentType.getValue() + "; "+charset;
    }
    @Builder(builderMethodName = "MailInfoBuilder")
    public MailInfo(@NotBlank @NonNull String mailInfoName, @NotNull @NonNull MailAddress mailFrom, @NotNull @NonNull MailAddress replyTo, @NotBlank @NonNull String charset, String encoding, @NotNull @NonNull ContentType contentType) {
        this.mailInfoName = mailInfoName;
        this.mailFrom = mailFrom;
        this.replyTo = replyTo;
        this.charset = charset;
        this.encoding = encoding;
        this.contentType = contentType;
    }

    public static MailInfoBuilder builder(MailInfoForm mailInfoForm) {
        return MailInfoBuilder()
                .mailInfoName(mailInfoForm.getMailInfoName())
                .mailFrom(mailInfoForm.getMailFrom())
                .replyTo(mailInfoForm.getReplyTo())
                .encoding(mailInfoForm.getEncoding())
                .charset(mailInfoForm.getCharset())
                .contentType(mailInfoForm.getContentType());
    }
    public void updateByMailInfoForm(MailInfoForm mailInfoForm) {
        this.mailInfoName = mailInfoForm.getMailInfoName();
        this.charset = mailInfoForm.getCharset();
        this.contentType = mailInfoForm.getContentType();
    }
}
