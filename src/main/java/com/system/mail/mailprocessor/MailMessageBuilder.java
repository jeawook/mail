package com.system.mail.mailprocessor;

import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendresultdetail.SendResultDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;

/**
 * SendInfo + 수신자별 SendResultDetail로부터 헤더/매크로 치환/인코딩이 모두 적용된
 * 실제 SMTP DATA 본문(헤더 + 본문)을 조립한다.
 */
@Component
@RequiredArgsConstructor
public class MailMessageBuilder {

    private final MacroProcessor macroProcessor;
    private final MailHeaderEncoder mailHeaderEncoder;
    private final MailProperties mailProperties;

    public String build(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeHeader(sendInfo, sendResultDetail));

        String content = macroProcess(sendInfo, sendResultDetail, sendInfo.getContent());
        if (sendInfo.getMailInfo().getEncoding().equals(ContentEncoding.BASE64)) {
            content = Base64.getMimeEncoder().encodeToString(content.getBytes());
        }

        sb.append(content);
        return sb.toString();
    }

    private String macroProcess(SendInfo sendInfo, SendResultDetail sendResultDetail, String target) {
        return macroProcessor.process(sendInfo.getMacroKey(), sendResultDetail.getMacroValue(), target);
    }

    private String makeHeader(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        StringBuilder sb = new StringBuilder();
        MailInfo mailInfo = sendInfo.getMailInfo();
        String charset = getCharset(mailInfo);

        String subject = macroProcess(sendInfo, sendResultDetail, sendInfo.getSubject());
        sb.append(encodeHeader(subject, charset));

        sb.append(encodeNameHeader(MailHeader.FROM, mailInfo.getHeaderFrom(), charset));
        sb.append(encodeNameHeader(MailHeader.REPLY_TO, mailInfo.getHeaderReply(), charset));
        sb.append(encodeNameHeader(MailHeader.TO, sendResultDetail.getMailAddress().getHeaderAddress(), charset));
        sb.append(createHeader(MailHeader.DATE.getValue(), LocalDateTime.now().toString()));
        sb.append(createHeader(MailHeader.CONTENT_TYPE.getValue(), mailInfo.getHeaderContentType()));
        sb.append(createHeader(MailHeader.CONTENT_TRANSFER_ENCODING.getValue(), mailInfo.getEncoding().getValue()));

        createHeaderProperties(sb);

        sb.append(MailHeader.CRLF.getValue());

        return sb.toString();
    }

    private void createHeaderProperties(StringBuilder sb) {
        Set<String> properties = mailProperties.getProperties();
        properties.forEach(propertyKey -> sb.append(createHeader(propertyKey, mailProperties.getProperty(propertyKey))));
    }

    private String getCharset(MailInfo mailInfo) {
        String charset = mailInfo.getCharset();
        if (charset == null || charset.equals("")) {
            return mailProperties.getProperty(MailHeader.CHARSET.getValue());
        }
        return charset;
    }

    private String encodeHeader(String value, String charset) {
        return mailHeaderEncoder.encode(MailHeader.SUBJECT.getValue(), value, charset);
    }

    private String encodeNameHeader(MailHeader mailHeader, String value, String charset) {
        return mailHeaderEncoder.encodeNameHeader(mailHeader.getValue(), value, charset);
    }

    private String createHeader(String mailHeader, String value) {
        return mailHeaderEncoder.create(mailHeader, value);
    }
}
