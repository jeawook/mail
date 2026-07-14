package com.system.mail.templete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MailTemplateRepositoryCustom {
    Page<MailTemplate> searchByName(String searchKey, Pageable pageable);
}
