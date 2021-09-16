package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mailGroup")
@RequiredArgsConstructor
public class MailGroupController {

    private final ModelMapper modelMapper;
    private final MailGroupService mailGroupService;

    @GetMapping("/mailGroupList")
    public String mailGroupList(@PageableDefault(size = 10, sort = "id")Pageable pageable, Model model) {
        Page<MailGroup> list = mailGroupService.findMailGroupList(pageable);
        model.addAttribute("mailGroupList", list);
        return "mailGroup/mailGroupList";
    }

    @GetMapping("/{mailGroupId}")
    public String mailGroup(@PathVariable Long mailGroupId, Model model) {
        Optional<MailGroup> mailGroupById = mailGroupService.findMailGroupById(mailGroupId);
        if (mailGroupById.isEmpty()) {
            return "mailGroup/mailGroupList";
        }
        model.addAttribute("mailGroup", mailGroupById.get());

        return "mailGroup/mailGroup";
    }

    @GetMapping("/{mailGroupId}/edit")
    public String editMailGroup(@PathVariable Long mailGroupId, Model model) {
        Optional<MailGroup> mailGroupById = mailGroupService.findMailGroupById(mailGroupId);
        if (mailGroupById.isEmpty()) {
            return "mailGroup/mailGroupList";
        }
        model.addAttribute("mailGroup", mailGroupById.get());
        return "mailGroup/editMailGroup";
    }

    @PostMapping("/{mailGroupId}/edit")
    public String edit(@PathVariable Long mailGroupId, @Validated @ModelAttribute("mailGroup") MailGroupForm mailGroupForm,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "mailGroup/editMailGroup";
        }

        return "redirect:/mailGroup/{mailGroupId}";
    }

    @GetMapping("/add")
    public String createMailGroup(@ModelAttribute("mailGroup") MailGroupForm mailGroupForm) {
        return "mailGroup/createMailGroup";
    }

    @PostMapping("/add")
    public String createMailGroup(@Validated @ModelAttribute("mailGroup") MailGroupForm mailGroupForm,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/MailGroup/createMailGroup";
        }
        MailGroup mailGroup = modelMapper.map(mailGroupForm, MailGroup.class);
        mailListFormToMailList(mailGroupForm.getMailListForms(), mailGroup.getMailLists());
        MailGroup saveMailGroup = mailGroupService.saveMailGroup(mailGroup);

        redirectAttributes.addAttribute("mailGroupId", saveMailGroup.getId());
        return "redirect:/MailGroup/{mailGroupId}";
    }

    private void  mailListFormToMailList(ArrayList<MailListForm> mailListForms, List<MailList> mailLists) {
        mailListForms.stream().map(mailListForm -> mailLists.add(modelMapper.map(mailListForm, MailList.class)));
    }


}
