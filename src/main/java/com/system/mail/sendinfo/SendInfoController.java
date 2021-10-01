package com.system.mail.sendinfo;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupService;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/sendInfo")
public class SendInfoController {

    private final SendInfoService sendInfoService;
    private final MailInfoService mailInfoService;
    private final MailGroupService mailGroupService;

    @ModelAttribute(name = "mailInfoList")
    public Map<Long, String> mailInfoList() {
        Map<Long, String> mailInfoMap = new HashMap<>();
        List<MailInfo> mailInfoAll = mailInfoService.findMailInfoAll();
        mailInfoAll.forEach(mailInfo -> mailInfoMap.put(mailInfo.getId(), mailInfo.getMailInfoName()));
        return mailInfoMap;
    }

    @ModelAttribute(name = "mailGroupList")
    public Map<Long, String> mailGroupList() {
        Map<Long, String> mailGroupMap = new HashMap<>();
        List<MailGroup> mailGroupAll = mailGroupService.findMailGroupAll();
        mailGroupAll.forEach(mailGroup -> mailGroupMap.put(mailGroup.getId(), mailGroup.getMailGroupName()));
        return mailGroupMap;
    }

    @GetMapping("/list")
    public String sendInfoList(@PageableDefault(size = 10)Pageable pageable, Model model) {

        Page<SendInfo> sendInfoList = sendInfoService.findSendInfoList(pageable);

        model.addAttribute("sendInfoList", sendInfoList);

        return "sendInfo/sendInfoList";
    }

    @GetMapping("/add")
    public String createSendInfo(@ModelAttribute(name = "sendInfo")SendInfoForm sendInfoForm) {
        return "sendInfo/createSendInfo";
    }

    @PostMapping("/add")
    public String create(@Validated @ModelAttribute(name = "sendInfo") SendInfoForm sendInfoForm,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        MailInfo mailInfo = mailInfoService.findMailInfoById(sendInfoForm.getMailInfoId());
        MailGroup mailGroup = mailGroupService.findMailGroupById(sendInfoForm.getMailGroupId());

        checkMailInfoNMailGroup(bindingResult, mailInfo, mailGroup);

        if (bindingResult.hasErrors()) {
            return "sendInfo/createSendInfo";
        }

        SendInfo sendInfo = SendInfo.SendInfo(sendInfoForm, mailInfo, mailGroup).build();
        Long sendInfoId = sendInfoService.saveSendInfo(sendInfo).getId();

        redirectAttributes.addAttribute("sendInfoId", sendInfoId);

        return "redirect:/senInfo/{sendInfoId}";
    }

    private void checkMailInfoNMailGroup(BindingResult bindingResult, MailInfo mailInfoById, MailGroup mailGroupById) {
        if (mailInfoById.getId() == null ) {
            bindingResult.reject("field-error", "메일 그룹이 잘못 되었습니다.");
        }
        if (mailGroupById.getId() == null) {
            bindingResult.reject("field-error", "메일 그룹이 잘못 되었습니다.");
        }
    }

}
