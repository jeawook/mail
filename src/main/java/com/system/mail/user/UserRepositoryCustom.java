package com.system.mail.user;

public interface UserRepositoryCustom {
    User findByIdAndGroupId(Long userId, Long groupId);
}
