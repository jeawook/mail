package com.system.mail.mailprocessor;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.User;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoService;
import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoService;
import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultService;
import com.system.mail.sendresultdetail.SendResultDetail;
import com.system.mail.sendresultdetail.SendResultDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MailProcessor {

    private static HashMap<String, Integer> connectionInfo = new HashMap<>();

    private final MailHeaderEncoder mailHeaderEncoder;
    private final SendInfoService sendInfoService;
    private final SendResultService sendResultService;
    private final SocketMailSender socketMailSender;

    @Transactional
    public void process(Long sendInfoId) {
        SendInfo sendInfo = sendInfoService.findSendInfoById(sendInfoId);
        MailGroup mailGroup = sendInfo.getMailGroup();

        SendResult sendResult = getSendResult(mailGroup);
        sendInfo.setSendResult(sendResult);

        makeHeader(sendInfo);

        sendInfo.mailStatusEnd();
    }

    private void makeHeader(SendInfo sendInfo) {
        StringBuffer sb = new StringBuffer();
        String charset = sendInfo.getMailInfo().getCharset();
        String subject = encodeHeader(MailHeader.SUBJECT, sendInfo.getSubject(), charset);
        String mailFrom = encodeHeader(MailHeader.FROM, sendInfo.getMailInfo().getHeaderFrom(), charset);
        String replyTo = encodeHeader(MailHeader.REPLY_TO, sendInfo.getMailInfo().getHeaderReply(), charset);
        encodeHeader(MailHeader.DATE, LocalDateTime.now().toString(), charset);


    }
    private String encodeHeader(MailHeader mailHeader, String value, String charset ) {
        return mailHeaderEncoder.encode(mailHeader.getValue(), value, charset);
    }

    private SendResult getSendResult(MailGroup mailGroup) {
        List<User> users = mailGroup.getUsers();
        return createResult(users);
    }

    @Transactional
    private SendResult createResult(List<User> users) {
        SendResult sendResult = SendResult.SendResult(users.size()).build();
        users.forEach(user -> sendResult.addSendResultDetail(SendResultDetail.SendResultDetail(user.getMailAddress()).build()));
        sendResultService.saveSendResult(sendResult);
        return sendResult;
    }



}
