package com.system.mail.sendresultdetail;

import com.system.mail.sendresult.SendResult;
import com.system.mail.sendresult.SendResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendResultDetailService {
    private final SendResultDetailRepository sendResultDetailRepository;

    public SendResultDetail findById(Long id) {
        return sendResultDetailRepository.findById(id).orElseGet(SendResultDetail::new);
    }

}
