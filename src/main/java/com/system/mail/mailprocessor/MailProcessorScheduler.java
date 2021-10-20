package com.system.mail.mailprocessor;

import com.system.mail.sendinfo.SendInfoService;
import com.system.mail.sendinfo.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Transactional
@RequiredArgsConstructor
public class MailProcessorScheduler {

    private final Status status = Status.WAIT;
    private final MailProcessor mailProcessor;
    private final SendInfoService sendInfoService;
    @Scheduled(cron = "*/10 * * * * *")
    public void mailScheduler() {

        System.out.println("mailScheduler "+ LocalDateTime.now());
    }
}
