package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
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
    private static final HashMap<String, Integer> connectionCnt = new HashMap<>();
    private final MacroProcessor macroProcessor;

    private final ConnectionManager connManager;

    private final MailProperties mailProperties;
    private final MailHeaderEncoder mailHeaderEncoder;
    private final SendInfoService sendInfoService;
    private final SendResultService sendResultService;
    private final SocketMailSender socketMailSender;

    @Transactional
    public void process(Long sendInfoId) {
        SendInfo sendInfo = sendInfoService.findById(sendInfoId);
        sendInfo.mailStatusSending();

        SendResult sendResult = createResult(sendInfo);

        sendInfo.setSendResult(sendResult);
        LinkedList<SendResultDetail> resultDetailLinkedList = new LinkedList<>(sendResult.getSendResultDetails());


        while (!resultDetailLinkedList.isEmpty()) {
            SendResultDetail sendResultDetail = resultDetailLinkedList.poll();
            String domain = sendResultDetail.getDomain();

            if (connManager.addConn(domain)) {

                SMTPResult smtpResult = socketMailSender.send(makeMailDTO(sendInfo, sendResultDetail));
                logger.info("smtpResult : "+smtpResult.getResultCode()+", "+smtpResult.getResultMessage());
                sendResultDetail.setResult(smtpResult);
                continue;
            }
            resultDetailLinkedList.add(sendResultDetail);
        }
        sendInfo.mailStatusComplete();
    }

    private MailDto makeMailDTO(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        String data = makeData(sendInfo, sendResultDetail);
        MailAddress mailFrom = sendInfo.getMailFrom();
        MailAddress rcpTo = sendResultDetail.getMailAddress();
        return MailDto.builder().rcpTo(rcpTo).mailFrom(mailFrom).data(data).build();
    }

    private String makeData(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
        MailInfo mailInfo = sendInfo.getMailInfo();
        String charset = getCharset(mailInfo);

        String subject = makeMacroProcess(sendInfo, sendResultDetail, sendInfo.getSubject());
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
    private String encodeHeader(String value, String charset ) {
        return mailHeaderEncoder.encode(MailHeader.SUBJECT.getValue(), value, charset);
    }
    private String encodeNameHeader(MailHeader mailHeader, String value, String charset ) {
        return mailHeaderEncoder.encodeNameHeader(mailHeader.getValue(), value, charset);
    }
    private String createHeader(String mailHeader, String value) {
        return mailHeaderEncoder.create(mailHeader, value);
    }


    @Transactional
    SendResult createResult(SendInfo sendInfo) {
        SendResult sendResult = SendResult.builder().sendInfo(sendInfo).build();
        sendResultService.saveSendResult(sendResult);
        return sendResult;
    }

}
