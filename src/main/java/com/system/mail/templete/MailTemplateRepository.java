package com.system.mail.templete;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailTemplateRepository extends JpaRepository<MailTemplate, Long>, MailTemplateRepositoryCustom {
}
