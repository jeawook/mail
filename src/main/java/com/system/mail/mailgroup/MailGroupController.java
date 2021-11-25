package com.system.mail.mailgroup;

import com.system.mail.common.MailAddress;
import com.system.mail.user.User;
import com.system.mail.user.UserForm;
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mailGroup")
@RequiredArgsConstructor
public class MailGroupController {

    private final ModelMapper modelMapper;
    private final MailGroupService mailGroupService;
    static final char MACRO_POINT_COMMA = ',';

    @GetMapping("/list")
    public String mailGroupList(Pageable pageable,String searchKey ,Model model) {
        Page<MailGroup> list = mailGroupService.findMailGroupList(null, pageable);
        model.addAttribute("mailGroupList", list);
        return "mailGroup/mailGroupList";
    }
    @GetMapping("/list/search")
    public String searchMailGroup(Pageable pageable,@RequestParam String searchKey, Model model) {
        Page<MailGroup> list = mailGroupService.findMailGroupList(searchKey, pageable);
        model.addAttribute("mailGroupList", list);
        return "mailGroup/mailGroupList";
    }

    @GetMapping("/{mailGroupId}")
    public String mailGroup(@PathVariable Long mailGroupId, Model model) {
        MailGroup mailGroupById = mailGroupService.findMailGroupById(mailGroupId);

        model.addAttribute("mailGroup", mailGroupById);

        return "mailGroup/mailGroup";
    }

    @GetMapping("/{mailGroupId}/edit")
    public String editMailGroup(@PathVariable Long mailGroupId, Model model) {
        MailGroup mailGroupById = mailGroupService.findMailGroupById(mailGroupId);
        model.addAttribute("mailGroup", mailGroupById);
        return "mailGroup/editMailGroup";
    }

    @PostMapping("/{mailGroupId}/edit")
    public String edit(@PathVariable Long mailGroupId, @Validated @ModelAttribute("mailGroup") MailGroupForm mailGroupForm,
                       BindingResult bindingResult, @ModelAttribute("del_id") ArrayList<Long> delUserIdxArr) {
        checkMacroValidation(mailGroupForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "mailGroup/editMailGroup";
        }

        MailGroup mailGroup = modelMapper.map(mailGroupForm, MailGroup.class);
        MailGroup findMailGroup = mailGroupService.findMailGroupById(mailGroupId);
        if (delUserIdxArr.size() != 0) {
            mailGroupService.deleteUser(delUserIdxArr);
        }
        mailGroupService.updateMailGroup(findMailGroup, mailGroup);
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
        setUser(mailGroup);
        MailGroup saveMailGroup = mailGroupService.saveMailGroup(mailGroup);

        redirectAttributes.addAttribute("mailGroupId", saveMailGroup.getId());
        return "redirect:/mailGroup/{mailGroupId}";
    }

    private void setUser(MailGroup mailGroup) {
        List<User> users = mailGroup.getUsers();
        if(users != null) {
            users.forEach(user -> user.setMailGroup(mailGroup));
        }
    }

    private void checkMacroValidation(MailGroupForm mailGroupForm, BindingResult bindingResult) {

        String macroKey = mailGroupForm.getMacroKey();
        int macroKeyCnt = countComma(macroKey);

        checkComma(bindingResult, macroKeyCnt, mailGroupForm.getUsers());

    }

    private void checkComma(BindingResult bindingResult, int macroKeyCnt, ArrayList<UserForm> userForms) {
        if (userForms != null) {
            for (UserForm tmp : userForms) {
                int userMacroCnt = countComma(tmp.getMacroValue());
                if (macroKeyCnt != userMacroCnt) {
                    bindingResult.reject("macroError", "macroKey 와 macroValue 의 개수는 동일 하게 입력 되어야 합니다.");
                    return;
                }
            }
        }
    }

    private int countComma(String macro) {
        if (macro == null || macro.length() <= 0) {
            return 0;
        }
        return (int) macro.chars().filter(c -> c == MACRO_POINT_COMMA).count();
    }

    /**
     * paging 및 test 확인을 위한 데이터
     */
//    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            MailGroup mailGroup = MailGroup.builder().mailGroupName("테스트그룹_"+i).macroKey("").build();
            mailGroupService.saveMailGroup(mailGroup);
        }
        MailAddress mailAddress = MailAddress.builder().name("박재욱").email("pdj13579@nate.com").build();
        MailGroup mailGroup = MailGroup.builder().mailGroupName("테스트그룹").macroKey("subject,content").build();
        User user = User.builder().mailAddress(mailAddress).macroValue("제목입니다,본문입니다").build();
        for (int i = 0; i < 10; i++) {
            mailGroup.addUser(user);
        }
        mailGroupService.saveMailGroup(mailGroup);
    }
}
