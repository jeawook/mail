package com.system.mail.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    User findByIdAndGroupId(Long userId, Long groupId);
    Page<User> findByGroupId(Long groupId, Pageable pageable);
}
