package com.system.mail.mailinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public MailInfo findMailInfoById(Long mailInfoId) {
        return mailInfoRepository.findById(mailInfoId).orElseGet(MailInfo::new);
    }

    public Page<MailInfo> findMailInfoListByPage(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable= PageRequest.of(page,10);
        return mailInfoRepository.findAll(pageable);
    }

    public List<MailInfo> findMailInfoAll(Sort sort) {
        return mailInfoRepository.findAll(sort);
    }
    @Transactional
    public void updateMailInfo(Long mailInfoId, MailInfoForm mailInfoForm) {
        MailInfo mailInfo = mailInfoRepository.findById(mailInfoId).orElseGet(MailInfo::new);
        mailInfo.updateByMailInfoForm(mailInfoForm);
    }

}
