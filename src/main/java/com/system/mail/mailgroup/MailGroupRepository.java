package com.system.mail.mailgroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailGroupRepository extends JpaRepository<MailGroup, Long>, MailGroupRepositoryCustom {
}
