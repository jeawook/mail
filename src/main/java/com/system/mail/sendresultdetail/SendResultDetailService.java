package com.system.mail.sendresultdetail;

import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendResultDetailService {
    private final SendResultDetailRepository sendResultDetailRepository;

    public Page<SendResultDetail> findBySendResultId(Long sendResultId, Pageable pageable) {
        return sendResultDetailRepository.findByResultId(sendResultId, pageable);
    }

    public Page<SendResultDetail> findByNameOrEmail(Long sendResultId, SendResultDetailSearchCond searchCond, Pageable pageable) {
        return sendResultDetailRepository.findByNameOrEmail(sendResultId, searchCond,pageable);
    }

}
