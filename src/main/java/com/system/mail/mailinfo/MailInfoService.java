package com.system.mail.mailinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
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

    public List<MailInfo> findMailInfoAll() {
        return mailInfoRepository.findAll();
    }
    @Transactional
    public void updateMailInfo(Long mailInfoId, MailInfoForm mailInfoForm) {
        Optional<MailInfo> mailInfoOptional = mailInfoRepository.findById(mailInfoId);

        MailInfo mailInfo = mailInfoOptional.get();
        mailInfo.setMailInfoName(mailInfoForm.getMailInfoName());
        mailInfo.setCharset(mailInfoForm.getCharset());
        mailInfo.setContentType(mailInfoForm.getContentType());
        mailInfo.setContent(mailInfoForm.getContent());

    }

}
