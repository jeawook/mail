package com.system.mail.mailinfo;

import com.system.mail.Mail;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Builder
public class MailInfo {

    @Id @GeneratedValue
    private Long id;

    @NotBlank
    private String subject;

    @NotNull
    private Mail mailFrom;

    @NotNull
    private Mail mailTo;

    @NotNull
    private Mail replyTo;

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
