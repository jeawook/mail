package com.system.mail.sendresult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendResultService {

    private final SendResultRepository sendResultRepository;

    public SendResult findSendResultById(Long id) {
        return sendResultRepository.findById(id).orElseGet(null);
    }
}
