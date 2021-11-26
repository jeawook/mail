package com.system.mail.sendresultdetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SendResultDetailRepositoryCustom {
    Page<SendResultDetail> findByResultId(Long resultId, Pageable pageable);
    Page<SendResultDetail> findByNameOrEmail(Long resultId, SendResultDetailSearchCond searchCond, Pageable pageable);
}
