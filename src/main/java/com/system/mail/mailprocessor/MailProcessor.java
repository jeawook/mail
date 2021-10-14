package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoService;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultService;
import com.system.mail.sendresultdetail.SendResultDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MailProcessor {

    private static HashMap<String, Integer> connectionCnt = new HashMap<>();
    private final MacroProcessor macroProcessor;
    private final HashMap<String, Integer> connectionInfo;
    private final MailProperties mailProperties;
    private final MailHeaderEncoder mailHeaderEncoder;
    private final SendInfoService sendInfoService;
    private final SendResultService sendResultService;
    private final SocketMailSender socketMailSender;
    private static final String DEFAULT_CONNECTION = "default";

    @Transactional
    public void process(Long sendInfoId) {
        SendInfo sendInfo = sendInfoService.findSendInfoById(sendInfoId);
        sendInfo.mailStatusSending();

        MailGroup mailGroup = sendInfo.getMailGroup();
        SendResult sendResult = createResult(mailGroup);

        sendInfo.setSendResult(sendResult);
        LinkedList<SendResultDetail> resultDetailLinkedList = (LinkedList<SendResultDetail>) sendResult.getSendResultDetails();

        while (!resultDetailLinkedList.isEmpty()) {
            SendResultDetail sendResultDetail = resultDetailLinkedList.poll();
            String domain = sendResultDetail.getDomain();

            if (isDomainConnectionCheck(domain)) {
                connectionCnt.put(domain, connectionCnt.get(domain)+1);

                SMTPResult smtpResult = socketMailSender.send(makeMailDTO(sendInfo, sendResultDetail));
                sendResultDetail.setResult(smtpResult);

                connectionCnt.put(domain, connectionCnt.get(domain)-1);
                continue;
            }
            resultDetailLinkedList.add(sendResultDetail);
        }
        sendInfo.mailStatusEnd();
    }
    private boolean isDomainConnectionCheck(String domain) {
        connectionCnt.putIfAbsent(domain, 0);
        return connectionInfo.getOrDefault(domain, connectionInfo.get(DEFAULT_CONNECTION)) > connectionCnt.get(domain);
    }

    private MailDTO makeMailDTO(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        String data = makeMailData(sendInfo, sendResultDetail);
        MailAddress mailFrom = sendInfo.getMailFrom();
        MailAddress rcpTo = sendResultDetail.getMailAddress();
        return MailDTO.mailDto(rcpTo, mailFrom, data).build();
    }

    private String makeMailData(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        StringBuffer sb = new StringBuffer();
        sb.append(makeHeader(sendInfo, sendResultDetail.getMailAddress()));
        String encoding = sendInfo.getMailInfo().getEncoding();
        String content = makeMacroContent(sendInfo, sendResultDetail);
        if (encoding.equals(ContentEncoding.BASE64.getValue())) {
            content = Arrays.toString(Base64.getMimeEncoder().encode(content.getBytes()));
        }
        sb.append(content);
        return sb.toString();
    }

    private String makeMacroContent(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        return macroProcessor.process(sendInfo.getMacroKey(), sendResultDetail.getMacroValue(), sendInfo.getContent());
    }

    private String makeHeader(SendInfo sendInfo, MailAddress mailTo) {
        StringBuffer sb = new StringBuffer();
        MailInfo mailInfo = sendInfo.getMailInfo();
        String charset = getCharset(mailInfo);

        sb.append(encodeHeader(MailHeader.SUBJECT, sendInfo.getSubject(), charset));
        sb.append(encodeHeader(MailHeader.DATE, LocalDateTime.now().toString(), charset));
        sb.append(encodeNameHeader(MailHeader.FROM, mailInfo.getHeaderFrom(), charset));
        sb.append(encodeNameHeader(MailHeader.REPLY_TO, mailInfo.getHeaderReply(), charset));
        sb.append(encodeNameHeader(MailHeader.TO, mailTo.getHeaderAddress(), charset));
        sb.append(createHeader(MailHeader.CONTENT_TYPE, mailInfo.getHeaderContentType()));
        sb.append(createHeader(MailHeader.CONTENT_TRANSFER_ENCODING, mailInfo.getEncoding()));
        createHeaderProperties(sb, charset);
        sb.append(MailHeader.CRLF);

        return sb.toString();
    }

    private void createHeaderProperties(StringBuffer sb, String charset) {
        Set<String> properties = mailProperties.getProperties();
        properties.forEach(propertyKey -> sb.append(mailHeaderEncoder.encode(propertyKey, mailProperties.getProperty(propertyKey), charset)));
    }

    private String getCharset(MailInfo mailInfo) {
        String charset = mailInfo.getCharset();
        if (charset == null || charset.equals("")) {
            return mailProperties.getProperty(MailHeader.CHARSET.getValue());
        }
        return charset;
    }
    private String encodeHeader(MailHeader mailHeader, String value, String charset ) {
        return mailHeaderEncoder.encode(mailHeader.getValue(), value, charset);
    }
    private String encodeNameHeader(MailHeader mailHeader, String value, String charset ) {
        return mailHeaderEncoder.encodeNameHeader(mailHeader.getValue(), value, charset);
    }
    private String createHeader(MailHeader mailHeader, String value) {
        return mailHeaderEncoder.create(mailHeader.getValue(), value);
    }


    @Transactional
    private SendResult createResult(MailGroup mailGroup) {
        SendResult sendResult = SendResult.SendResult(mailGroup).build();
        sendResult.createSendResultDetails(mailGroup.getUsers());
        sendResultService.saveSendResult(sendResult);
        return sendResult;
    }



}
