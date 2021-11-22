package com.system.mail.sendresult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SendResultRepositoryCustom {
    ResultInfoDto findSendResult(Long id);
}
