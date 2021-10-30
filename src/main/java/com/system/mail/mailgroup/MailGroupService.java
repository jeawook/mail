package com.system.mail.mailgroup;

import com.system.mail.user.User;
import com.system.mail.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailGroupService {

    private final MailGroupRepository mailGroupRepository;
    private final UserRepository userRepository;
    public MailGroup saveMailGroup(MailGroup mailGroup) {
        return mailGroupRepository.save(mailGroup);
    }

    public MailGroup findMailGroupById(Long id) {
        return mailGroupRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public Page<MailGroup> findMailGroupList(Pageable pageable) {
        return mailGroupRepository.findAll(pageable);
    }

    public List<MailGroup> findMailGroupAll(Sort orders) {
        return mailGroupRepository.findAll(orders);
    }
    @Transactional
    public void updateMailGroup(Long id, MailGroup mailGroup) {
        MailGroup findMailGroup = mailGroupRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        findMailGroup.setMacroKey(mailGroup.getMacroKey());
        findMailGroup.setMailGroupName(mailGroup.getMailGroupName());
        updateUser(mailGroup, findMailGroup);
    }

    private void updateUser( MailGroup mailGroup, MailGroup findMailGroup) {
        for (User user : mailGroup.getUsers()) {
            if (user.getId() == null) {
                findMailGroup.addUser(user);
                continue;
            }
            User findUser = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
            findUser.setMacroValue(user.getMacroValue());
            findUser.setMailAddress(user.getMailAddress());
        }
    }


}
