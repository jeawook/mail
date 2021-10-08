package com.system.mail.sendinfo;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

}
