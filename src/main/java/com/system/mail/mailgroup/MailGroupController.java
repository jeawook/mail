package com.system.mail.mailgroup;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/MailGroup")
@RequiredArgsConstructor
public class MailGroupController {

    private final ModelMapper modelMapper;
    private final MailGroupService mailGroupService;

    @GetMapping("/MailGroupList")
    public String mailGroupList(@PageableDefault(size = 10, sort = "id")Pageable pageable, Model model) {
        Page<MailGroup> list = mailGroupService.findList(pageable);
        model.addAttribute("mailGroupList", list);
        return "mailGroup/mailGroupList";
    }
    @GetMapping("/{mailGroupId}")
    public String mailGroup(@PathVariable Long id, Model model) {
        Optional<MailGroup> mailGroupById = mailGroupService.findMailGroupById(id);
        if (mailGroupById.isEmpty()) {
            return "mailGroup/MailGroupList";
        }
        model.addAttribute("mailGroup", mailGroupById.get());

        return "mailGroup/mailGroup";
    }
    @PostMapping
    public String createMailGroup(@Validated @ModelAttribute("mailGroup") MailGroupForm mailGroupForm,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/MailGroup/createMailGroup";
        }
        MailGroup mailGroup = modelMapper.map(mailGroupForm, MailGroup.class);
        MailGroup saveMailGroup = mailGroupService.saveMailGroup(mailGroup);

        redirectAttributes.addAttribute("mailGroupId", saveMailGroup.getId());
        return "redirect:/MailGroup/{mailGroupId}";
    }


}
