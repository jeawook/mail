package com.system.mail.mailinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     *  mailInfoList 조회 page 사용
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String mailInfoList(@PageableDefault(size = 10, sort = "id") Pageable pageable, Model model) {
        Page<MailInfo> mailInfoList = mailInfoService.findMailInfoList(pageable);
        model.addAttribute("mailInfoList", mailInfoList);
        return "mailInfo/mailInfoList";
    }

    /**
     * mailInfo 조회 mailInfo 의 primary key 로 조회
     * @param mailInfoId
     * @param model
     * @return
     */

    @GetMapping("/{mailInfoId}")
    public String mailInfo(@PathVariable Long mailInfoId, Model model) {
        MailInfo mailInfo = mailInfoService.findMailInfoById(mailInfoId);
        if (mailInfo == null) {
            return "mailInfo/mailInfoList";
        }

        model.addAttribute("mailInfo", mailInfo);
        return "mailInfo/mailInfo";
    }

    @GetMapping("/{mailInfoId}/edit")
    public String editForm(@PathVariable Long mailInfoId, Model model) {
        MailInfo mailInfo = mailInfoService.findMailInfoById(mailInfoId);
        if (mailInfo.getId() == null) {
            return "mailInfo/mailInfoList";
        }
        model.addAttribute("mailInfo", mailInfo);
        return "mailInfo/editMailInfo";
    }

    @PostMapping("/{mailInfoId}/edit")
    public String edit(@PathVariable Long mailInfoId,
                       @Validated @ModelAttribute(name = "mailInfo") MailInfoForm mailInfoForm,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println("mailInfoId = " + mailInfoId);
            return "mailInfo/editMailInfo";
        }
        MailInfo mailInfoById = mailInfoService.findMailInfoById(mailInfoId);
        if (mailInfoById == null) {
            return "mailInfo/mailInfoList";
        }

        mailInfoService.updateMailInfo(mailInfoId, mailInfoForm);

        return "redirect:/mailInfo/{mailInfoId}";

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
