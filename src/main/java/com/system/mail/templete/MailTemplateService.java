package com.system.mail.templete;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailTemplateService {

    private final MailTemplateRepository mailTemplateRepository;

    public MailTemplate saveMailTemplate(MailTemplate mailTemplate) {
        return mailTemplateRepository.save(mailTemplate);
    }

    public MailTemplate findById(Long id) {
        return mailTemplateRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public Page<MailTemplate> findMailTemplateListByPage(Pageable pageable) {
        return mailTemplateRepository.findAll(pageable);
    }

    public Page<MailTemplate> findMailTemplateByName(String searchKey, Pageable pageable) {
        return mailTemplateRepository.searchByName(searchKey, pageable);
    }

    public List<MailTemplate> findMailTemplateAll(Sort sort) {
        return mailTemplateRepository.findAll(sort);
    }

    @Transactional
    public void updateMailTemplate(Long id, MailTemplate mailTemplate) {
        MailTemplate findMailTemplate = mailTemplateRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        findMailTemplate.updateByForm(mailTemplate);
    }
}
