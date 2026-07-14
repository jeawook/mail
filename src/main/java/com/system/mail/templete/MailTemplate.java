package com.system.mail.templete;

import com.system.mail.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import static com.mysema.commons.lang.Assert.notNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
public class MailTemplate extends BaseTimeEntity {

    @GeneratedValue @Id
    @Column(name = "mail_template_id")
    private Long id;

    @NotBlank
    @Length(max = 255, message = "템플릿명은 최대 255자")
    private String templateName;

    @Length(max = 255, message = "제목은 최대 255자")
    private String subject;

    @NotBlank
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    // 메일 그룹의 macroKey 와 동일한 형식의 콤마 구분 키 목록
    private String macroKey;

    @Builder
    public MailTemplate(String templateName, String subject, String content, String macroKey) {
        notNull(templateName, "templateName must not be null");
        notNull(content, "content must not be null");

        this.templateName = templateName;
        this.subject = subject;
        this.content = content;
        this.macroKey = macroKey;
    }

    public void updateByForm(MailTemplate mailTemplate) {
        this.templateName = mailTemplate.getTemplateName();
        this.subject = mailTemplate.getSubject();
        this.content = mailTemplate.getContent();
        this.macroKey = mailTemplate.getMacroKey();
    }
}
