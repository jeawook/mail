package com.system.mail.mailinfo;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/mailInfo")
@RequiredArgsConstructor
public class MailInfoController {

    private final MailInfoService mailInfoService;

    @GetMapping
    public String mailInfoList(@PageableDefault(size = 10, sort = "id") Pageable pageable, Model model) {
        Page<MailInfo> mailInfoList = mailInfoService.findMailInfoList(pageable);
        return "mailInfo/mailInfoList";
    }

    @GetMapping("/{mailInfoId}")
    public String mailInfo(@PathVariable Long mailInfoId, Model model) {
        Optional<MailInfo> mailInfo = mailInfoService.findMailInfoById(mailInfoId);
        if (mailInfo.isEmpty()) {
            return "mailInfo/ailInfoList";
        }
        model.addAttribute("mailInfo", mailInfo.get());
        return "mailInfo/mailInfo";
    }

    @GetMapping("/add")
    public String mailInfo(@ModelAttribute(name = "mailInfo") MailInfoForm mailInfoForm) {
        return "mailInfo/createMailInfo";
    }

    @PostMapping("/add")
    public String createMailInfo(@Validated @ModelAttribute(name = "mailInfo") MailInfoForm mailInfoForm,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "mailInfo/createMailInfo";
        }
        MailInfo mailInfo = MailInfo.builder(mailInfoForm).build();
        MailInfo saveMailInfo = mailInfoService.saveMailInfo(mailInfo);

        redirectAttributes.addAttribute("mailInfoId", saveMailInfo.getId());

        return "redirect:/mailInfo/{mailInfoId}";
    }
}
