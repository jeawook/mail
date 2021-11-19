package com.system.mail.mailgroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MailGroupRepositoryCustom {

    Page<MailGroup> searchByName(String mailGroupName, Pageable pageable);
}
