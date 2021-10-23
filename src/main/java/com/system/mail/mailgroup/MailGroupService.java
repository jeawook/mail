package com.system.mail.mailgroup;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailGroupService {

    private final MailGroupRepository mailGroupRepository;

    public MailGroup saveMailGroup(MailGroup mailGroup) {
        return mailGroupRepository.save(mailGroup);
    }

    public MailGroup findMailGroupById(Long id) {
        return mailGroupRepository.findById(id).orElseGet(MailGroup::new);
    }

    public Page<MailGroup> findMailGroupList(Pageable pageable) {
        return mailGroupRepository.findAll(pageable);
    }

    public List<MailGroup> findMailGroupAll() {
        return mailGroupRepository.findAll();
    }
    @Transactional
    public void updateMailGroup(Long id, MailGroup mailGroup) {
        MailGroup findMailGroup = mailGroupRepository.findById(id).orElseGet(MailGroup::new);
        findMailGroup.setMacroKey(mailGroup.getMacroKey());
        findMailGroup.setMailGroupName(mailGroup.getMailGroupName());
        findMailGroup.setUsers(mailGroup.getUsers());
    }
}
