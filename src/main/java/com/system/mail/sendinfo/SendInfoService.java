package com.system.mail.sendinfo;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SendInfoService {

    private final SendInfoRepository sendInfoRepository;

    public SendInfo save(SendInfo sendInfo) {
        return sendInfoRepository.save(sendInfo);
    }

    public SendInfoDto findById(Long id) {
        return sendInfoRepository.findSendInfoDtoById(id);
    }


    public Page<SendInfo> findSendInfoList(Pageable pageable) {
        return sendInfoRepository.findAll(pageable);
    }

    public Page<SendInfo> findSendInfoListBySubject(Pageable pageable, String subject, LocalDateTime startDate, LocalDateTime endDate) {
        return sendInfoRepository.findByDateAndSubject(pageable, subject, startDate, endDate);
    }

    public SendInfo findSendInfo(Status status, LocalDateTime sendDate) {
        return sendInfoRepository.findByStatusAndSendTime(status, sendDate);
    }

    @Transactional
    public void mailRegistering(Long id) {
        SendInfo sendInfo = sendInfoRepository.findById(id).orElseGet(SendInfo::new);
        sendInfo.mailRegister();
    }

    @Transactional
    public void updateSendInfo(Long sendInfoId, SendInfo sendInfo) {
        SendInfo findSendInfo = sendInfoRepository.findById(sendInfoId).orElseGet(SendInfo::new);
        findSendInfo.setMailGroup(sendInfo.getMailGroup());
        findSendInfo.setSendDate(sendInfo.getSendDate());
        findSendInfo.setSubject(sendInfo.getSubject());
        findSendInfo.setContent(sendInfo.getContent());
    }
}
