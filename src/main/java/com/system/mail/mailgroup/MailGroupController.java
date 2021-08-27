package com.system.mail.mailgroup;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/MailGroup/new")
@RequiredArgsConstructor
public class MailGroupController {

    private final ModelMapper modelMapper;
    private final MailGroupService mailGroupService;
    @PostMapping
    public String createMailGroup(@Validated @ModelAttribute("mailGroup") MailGroupForm mailGroupForm,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        MailGroup mailGroup = modelMapper.map(mailGroupForm, MailGroup.class);
        MailGroup saveMailGroup = mailGroupService.saveMailGroup(mailGroup);

        redirectAttributes.addAttribute("mailGroupId", saveMailGroup.getId());
        return "redirect:/MailGroup/{mailGroupId}";
    }
}
