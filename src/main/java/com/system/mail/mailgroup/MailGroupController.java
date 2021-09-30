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

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mailGroup")
@RequiredArgsConstructor
public class MailGroupController {

    private final ModelMapper modelMapper;
    private final MailGroupService mailGroupService;

    static char MACRO_POINT_COMMA = ',';

    @GetMapping("/list")
    public String mailGroupList(@PageableDefault(size = 10, sort = "id")Pageable pageable, Model model) {
        Page<MailGroup> list = mailGroupService.findMailGroupList(pageable);
        model.addAttribute("mailGroupList", list);
        return "mailGroup/mailGroupList";
    }

    @GetMapping("/{mailGroupId}")
    public String mailGroup(@PathVariable Long mailGroupId, Model model) {
        Optional<MailGroup> mailGroupById = mailGroupService.findMailGroupById(mailGroupId);
        if (mailGroupById.isEmpty()) {
            return "redirect:mailGroup/mailGroupList";
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

        checkMacroValidation(mailGroupForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/MailGroup/createMailGroup";
        }

        MailGroup mailGroup = modelMapper.map(mailGroupForm, MailGroup.class);
        mailGroup.setUsers(userFormListToUserList(mailGroupForm.getUserForms(), mailGroup));
        MailGroup saveMailGroup = mailGroupService.saveMailGroup(mailGroup);

        redirectAttributes.addAttribute("mailGroupId", saveMailGroup.getId());
        return "redirect:/mailGroup/{mailGroupId}";
    }

    private void checkMacroValidation(MailGroupForm mailGroupForm, BindingResult bindingResult) {

        String macroKey = mailGroupForm.getMacroKey();
        int macroKeyCnt = countComma(macroKey);
        checkComma(bindingResult, macroKeyCnt, mailGroupForm.getUserForms());

    }

    private void checkComma(BindingResult bindingResult, int macroKeyCnt, ArrayList<UserForm> userForms) {
        for (UserForm tmp : userForms) {
            int userMacroCnt = countComma(tmp.getMacroValue());
            if (macroKeyCnt != userMacroCnt) {
                bindingResult.reject("macroError", "macroKey 와 macroValue 의 개수는 동일 하게 입력 되어야 합니다.");
                return;
            }
        }
    }

    private int countComma(String macro) {
        return (int) macro.chars().filter(c -> c == MACRO_POINT_COMMA).count();
    }

    private ArrayList<User> userFormListToUserList(ArrayList<UserForm> userFormList, MailGroup mailGroup) {
        return new ArrayList<>(userFormList.stream().map(userForm -> modelMapper.map(userForm, User.class)).collect(Collectors.toList()));
    }
}
