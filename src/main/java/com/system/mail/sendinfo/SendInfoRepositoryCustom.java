package com.system.mail.sendinfo;

import java.time.LocalDateTime;

public interface SendInfoRepositoryCustom {
    SendInfo findByStatusAndSendTime(Status status, LocalDateTime localDateTime);
}
