package com.system.mail.mailgroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailGroupService {

    private MailGroupRepository mailGroupRepository;

    public MailGroup saveMailGroup(MailGroup mailGroup) {
        return mailGroupRepository.save(mailGroup);
    }

    public List
}
