package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailinfo.ContentEncoding;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoService;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultService;
import com.system.mail.sendresultdetail.SendResultDetail;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MailProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static HashMap<String, Integer> connectionCnt = new HashMap<>();
    private final MacroProcessor macroProcessor;
    private final DomainConnectionProperties domainConnectionProperties;
    private final MailProperties mailProperties;
    private final MailHeaderEncoder mailHeaderEncoder;
    private final SendInfoService sendInfoService;
    private final SendResultService sendResultService;
    private final SocketMailSender socketMailSender;

    @Transactional
    public void process(Long sendInfoId) {
        SendInfo sendInfo = sendInfoService.findSendInfoById(sendInfoId);
        sendInfo.mailStatusSending();

        MailGroup mailGroup = sendInfo.getMailGroup();
        SendResult sendResult = createResult(mailGroup);

        sendInfo.setSendResult(sendResult);
        LinkedList<SendResultDetail> resultDetailLinkedList = new LinkedList<>(sendResult.getSendResultDetails());


        while (!resultDetailLinkedList.isEmpty()) {
            SendResultDetail sendResultDetail = resultDetailLinkedList.poll();
            String domain = sendResultDetail.getDomain();

            if (isDomainConnectionCheck(domain)) {
                connectionCnt.put(domain, connectionCnt.get(domain)+1);

                SMTPResult smtpResult = socketMailSender.send(makeMailDTO(sendInfo, sendResultDetail));
                logger.info("smtpResult : "+smtpResult.getResultCode()+", "+smtpResult.getResultMessage());
                sendResultDetail.setResult(smtpResult);

                connectionCnt.put(domain, connectionCnt.get(domain)-1);
                continue;
            }
            resultDetailLinkedList.add(sendResultDetail);
        }
        sendInfo.mailStatusComplete();
    }
    private boolean isDomainConnectionCheck(String domain) {
        connectionCnt.putIfAbsent(domain, 0);
        return  connectionCnt.get(domain) < domainConnectionProperties.getConnection(domain);
    }

    private MailDTO makeMailDTO(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        String data = makeData(sendInfo, sendResultDetail);
        MailAddress mailFrom = sendInfo.getMailFrom();
        MailAddress rcpTo = sendResultDetail.getMailAddress();
        return MailDTO.builder(rcpTo, mailFrom, data).build();
    }

    private String makeData(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        StringBuffer sb = new StringBuffer();
        sb.append(makeHeader(sendInfo, sendResultDetail));

        String content = makeMacroProcess(sendInfo, sendResultDetail, sendInfo.getContent());
        if (sendInfo.getMailInfo().getEncoding().equals(ContentEncoding.BASE64)) {
            content = Base64.getMimeEncoder().encodeToString(content.getBytes());
        }

        sb.append(content);
        return sb.toString();
    }

    private String makeMacroProcess(SendInfo sendInfo, SendResultDetail sendResultDetail, String target) {
        return macroProcessor.process(sendInfo.getMacroKey(), sendResultDetail.getMacroValue(), target);
    }

    private String makeHeader(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        StringBuffer sb = new StringBuffer();
        MailInfo mailInfo = sendInfo.getMailInfo();
        String charset = getCharset(mailInfo);

        String subject = makeMacroProcess(sendInfo, sendResultDetail, sendInfo.getSubject());
        sb.append(encodeHeader(MailHeader.SUBJECT, subject, charset));

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

    private void createHeaderProperties(StringBuffer sb) {
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
    private String encodeHeader(MailHeader mailHeader, String value, String charset ) {
        return mailHeaderEncoder.encode(mailHeader.getValue(), value, charset);
    }
    private String encodeNameHeader(MailHeader mailHeader, String value, String charset ) {
        return mailHeaderEncoder.encodeNameHeader(mailHeader.getValue(), value, charset);
    }
    private String createHeader(String mailHeader, String value) {
        return mailHeaderEncoder.create(mailHeader, value);
    }


    @Transactional
    private SendResult createResult(MailGroup mailGroup) {
        SendResult sendResult = SendResult.builder(mailGroup).build();
        sendResult.createSendResultDetails(mailGroup.getUsers());
        sendResultService.saveSendResult(sendResult);
        return sendResult;
    }

}
