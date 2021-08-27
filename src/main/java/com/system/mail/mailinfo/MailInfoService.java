package com.system.mail.mailinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailInfoService {

    private final MailInfoRepository mailInfoRepository;

    public MailInfo saveMailInfo(MailInfo mailInfo) {
        return mailInfoRepository.save(mailInfo);
    }

    public Optional<MailInfo> findMailInfoById(Long mailInfoId) {
        return mailInfoRepository.findById(mailInfoId);
    }

    public Page<MailInfo> findMailInfoList(Pageable pageable) {
        return mailInfoRepository.findAll(pageable);
    }
}
