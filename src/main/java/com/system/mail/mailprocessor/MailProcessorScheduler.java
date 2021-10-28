package com.system.mail.mailprocessor;

import com.system.mail.sendinfo.SendInfo;
import com.system.mail.sendinfo.SendInfoService;
import com.system.mail.sendinfo.Status;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Transactional
@RequiredArgsConstructor
public class MailProcessorScheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Status status = Status.REGISTER;
    private final MailProcessor mailProcessor;
    private final SendInfoService sendInfoService;
    @Scheduled(cron = "*/10 * * * * *")
    public void mailScheduler() {
        LocalDateTime nowDate = LocalDateTime.now();
        logger.info("mailScheduler "+ nowDate);
        SendInfo sendInfo = sendInfoService.findSendInfo(status, nowDate);

        if (sendInfo != null) {
            logger.info("find sendInfo id : "+sendInfo.getId());
            mailProcessor.process(sendInfo.getId());
        }

    }
}
