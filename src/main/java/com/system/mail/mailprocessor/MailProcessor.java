package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
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

import java.util.LinkedList;

@Component
@RequiredArgsConstructor
public class MailProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 도메인 동시 연결 수 제한에 걸려 재큐잉될 때 바쁜 대기(busy-wait)로 CPU를 스핀하지 않도록 두는 지연
    private static final long RETRY_DELAY_MS = 200L;

    private final ConnectionManager connManager;
    private final MailMessageBuilder mailMessageBuilder;
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
            requeue(resultDetailLinkedList, sendResultDetail);
        }
        sendInfo.mailStatusComplete();
    }

    private void requeue(LinkedList<SendResultDetail> resultDetailLinkedList, SendResultDetail sendResultDetail) {
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        resultDetailLinkedList.add(sendResultDetail);
    }

    private MailDto makeMailDTO(SendInfo sendInfo, SendResultDetail sendResultDetail) {
        String data = mailMessageBuilder.build(sendInfo, sendResultDetail);
        MailAddress mailFrom = sendInfo.getMailFrom();
        MailAddress rcpTo = sendResultDetail.getMailAddress();
        return MailDto.builder().rcpTo(rcpTo).mailFrom(mailFrom).data(data).build();
    }

    @Transactional
    public SendResult createResult(SendInfo sendInfo) {
        SendResult sendResult = SendResult.builder().sendInfo(sendInfo).build();
        sendResultService.saveSendResult(sendResult);
        return sendResult;
    }

}
