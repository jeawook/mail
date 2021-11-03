package com.system.mail.sendinfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SendInfoRepositoryCustom {
    SendInfo findByStatusAndSendTime(Status status, LocalDateTime localDateTime);
    Page<SendInfo> findByDateAndSubject(LocalDateTime data, String subject, Pageable pageable);
}
