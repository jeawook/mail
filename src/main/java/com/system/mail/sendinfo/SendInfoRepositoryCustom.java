package com.system.mail.sendinfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SendInfoRepositoryCustom {
    SendInfo findByStatusAndSendTime(Status status, LocalDateTime localDateTime);
    Page<SendInfo> findByDateAndSubject(Pageable pageable, String subject, LocalDateTime startDate, LocalDateTime endDate);
    SendInfoDto findSendInfoDtoById(Long id);
}
