package com.system.mail.sendresult;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SendResultRepository extends JpaRepository<SendResult, Long>, SendResultRepositoryCustom {
}
