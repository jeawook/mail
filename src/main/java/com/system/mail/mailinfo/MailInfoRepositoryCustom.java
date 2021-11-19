package com.system.mail.mailinfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MailInfoRepositoryCustom {
    Page<MailInfo> searchByName(String searchKey, Pageable pageable);
}
