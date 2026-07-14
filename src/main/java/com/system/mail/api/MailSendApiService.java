package com.system.mail.api;

import com.system.mail.common.MailAddress;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoService;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoService;
import com.system.mail.sendinfo.SendRecipient;
import com.system.mail.sendinfo.Status;
import com.system.mail.templete.MailTemplate;
import com.system.mail.templete.MailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailSendApiService {

    private static final char MACRO_COMMA = ',';

    private final MailTemplateService mailTemplateService;
    private final MailInfoService mailInfoService;
    private final SendInfoService sendInfoService;

    @Transactional
    public MailSendResponse send(MailSendRequest request) {
        MailTemplate mailTemplate = findTemplate(request.getTemplateId());
        MailInfo mailInfo = findMailInfo(request.getMailInfoId());

        validateMacro(mailTemplate.getMacroKey(), request.getRecipients());

        SendInfo sendInfo = SendInfo.builder()
                .sendDate(request.getSendDate() != null ? request.getSendDate() : LocalDateTime.now())
                .mailInfo(mailInfo)
                .subject(mailTemplate.getSubject())
                .content(mailTemplate.getContent())
                .macroKey(mailTemplate.getMacroKey())
                .status(Status.REGISTER)
                .build();

        request.getRecipients().forEach(recipient -> sendInfo.addRecipient(buildRecipient(recipient)));

        sendInfoService.save(sendInfo);

        return new MailSendResponse(sendInfo.getId(), sendInfo.getStatus().name());
    }

    private SendRecipient buildRecipient(RecipientRequest recipient) {
        MailAddress mailAddress = MailAddress.builder()
                .name(recipient.getName())
                .email(recipient.getEmail())
                .build();
        return SendRecipient.builder()
                .mailAddress(mailAddress)
                .macroValue(recipient.getMacroValue() == null ? "" : recipient.getMacroValue())
                .build();
    }

    private void validateMacro(String macroKey, List<RecipientRequest> recipients) {
        int macroKeyCnt = countComma(macroKey);
        for (RecipientRequest recipient : recipients) {
            int macroValueCnt = countComma(recipient.getMacroValue());
            if (macroKeyCnt != macroValueCnt) {
                throw new IllegalArgumentException(
                        "macroKey 와 macroValue 의 콤마 개수는 동일하게 입력되어야 합니다. email=" + recipient.getEmail());
            }
        }
    }

    private int countComma(String value) {
        if (value == null || value.length() == 0) {
            return 0;
        }
        return (int) value.chars().filter(c -> c == MACRO_COMMA).count();
    }

    private MailTemplate findTemplate(Long id) {
        try {
            return mailTemplateService.findById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 templateId 입니다: " + id);
        }
    }

    private MailInfo findMailInfo(Long id) {
        try {
            return mailInfoService.findMailInfoById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 mailInfoId 입니다: " + id);
        }
    }
}
