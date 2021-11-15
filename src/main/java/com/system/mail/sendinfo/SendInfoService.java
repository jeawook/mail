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

    public SendInfo saveSendInfo(SendInfo sendInfo) {
        return sendInfoRepository.save(sendInfo);
    }

    public SendInfo findSendInfoById(Long id) {
        return sendInfoRepository.findById(id).orElseGet(SendInfo::new);
    }


    public Page<SendInfo> findSendInfoList(Pageable pageable) {
        return sendInfoRepository.findAll(pageable);
    }

    public Page<SendInfo> findSendInfoListBySubject(Pageable pageable, String subject) {

    }

    public Page<SendInfo> findSendInfoListByDate(Pageable pageable, LocalDateTime sendDate) {

    }

    public SendInfo findSendInfo(Status status, LocalDateTime sendDate) {
        return sendInfoRepository.findByStatusAndSendTime(status, sendDate).orElse(null);
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
