package com.system.mail.sendresult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendResultService {

    private final SendResultRepository sendResultRepository;

    public SendResult saveSendResult(SendResult sendResult) {
        return sendResultRepository.save(sendResult);
    }
    public SendResult findById(Long id) {
        return sendResultRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
