package com.system.mail.sendinfo;

import com.system.mail.mailgroup.MailGroup;
import com.system.mail.mailgroup.MailGroupService;
import com.system.mail.mailinfo.MailInfo;
import com.system.mail.mailinfo.MailInfoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/sendInfo")
public class SendInfoController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SendInfoService sendInfoService;
    private final MailInfoService mailInfoService;
    private final MailGroupService mailGroupService;
    private final ModelMapper modelMapper;

    @ModelAttribute(name = "mailInfoList")
    public Map<Long, String> mailInfoList() {
        Map<Long, String> mailInfoMap = new HashMap<>();
        Sort id = Sort.by(Sort.Direction.DESC, "id");
        List<MailInfo> mailInfoAll = mailInfoService.findMailInfoAll(id);
        mailInfoAll.forEach(mailInfo -> mailInfoMap.put(mailInfo.getId(), mailInfo.getMailInfoName()));
        return mailInfoMap;
    }

    @ModelAttribute(name = "mailGroupList")
    public Map<Long, String> mailGroupList() {
        Map<Long, String> mailGroupMap = new HashMap<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<MailGroup> mailGroupAll = mailGroupService.findMailGroupAll(sort);
        mailGroupAll.forEach(mailGroup -> mailGroupMap.put(mailGroup.getId(), mailGroup.getMailGroupName()));
        return mailGroupMap;
    }

    @GetMapping("/list")
    public String sendInfoList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable, String subject, Model model) {

        Page<SendInfo> sendInfoList = sendInfoService.findSendInfoList(pageable);

        model.addAttribute("sendInfoList", sendInfoList);

        return "sendInfo/sendInfoList";
    }

    @GetMapping("/{id}")
    public String sendInfo(@PathVariable Long id, Model model) {
        SendInfoDto byId = sendInfoService.findSendInfoDtoById(id);

        model.addAttribute("sendInfo", byId);

        return "sendInfo/sendInfo";
    }

    @GetMapping("/add")
    public String mapToSendInfo(@ModelAttribute(name = "sendInfo")SendInfoForm sendInfoForm) {
        return "sendInfo/createSendInfo";
    }

    @PostMapping("/add")
    public String create(@Validated @ModelAttribute(name = "sendInfo") SendInfoForm sendInfoForm,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "sendInfo/createSendInfo";
        }

        MailInfo mailInfo = mailInfoService.findMailInfoById(sendInfoForm.getMailInfoId());
        MailGroup mailGroup = mailGroupService.findMailGroupById(sendInfoForm.getMailGroupId());

        if (isNullCheck(bindingResult, mailInfo, "mailInfoId")
                ||isNullCheck(bindingResult, mailGroup, "mailGroupId")) {
            return "sendInfo/createSendInfo";
        }

        SendInfo sendInfo = mapToSendInfo(sendInfoForm, mailInfo, mailGroup);

        Long sendInfoId = sendInfoService.save(sendInfo).getId();

        redirectAttributes.addAttribute("sendInfoId", sendInfoId);

        return "redirect:/sendInfo/{sendInfoId}";
    }


    @PostMapping("/{sendInfoId}/send")
    public String sendMail(@PathVariable Long sendInfoId) {
        logger.info("sendMail");
        SendInfo sendInfoById = sendInfoService.findById(sendInfoId);
        if (sendInfoById.getId() == null) {
            return "redirect:/sendInfo/list";
        }
        sendInfoService.mailRegistering(sendInfoById.getId());

        return "redirect:/sendInfo/{sendInfoId}";
    }

    @GetMapping("/{sendInfoId}/edit")
    public String updateSendInfoForm(@PathVariable Long sendInfoId, Model model) {

        SendInfo sendInfo = sendInfoService.findById(sendInfoId);
        if (sendInfo == null) {
            return "redirect:/sendInfo/list";
        }
        model.addAttribute("sendInfo", mapToSendInfoForm(sendInfo));

        return "sendInfo/editSendInfo";
    }

    @PostMapping("/{sendInfoId}/edit")
    public String updateSendInfo(@PathVariable Long sendInfoId,@Validated @ModelAttribute("sendInfo") SendInfoForm sendInfoForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sendInfo/editSendInfo";
        }
        SendInfo sendInfoById = sendInfoService.findById(sendInfoId);
        MailInfo mailInfo = mailInfoService.findMailInfoById(sendInfoForm.getMailInfoId());
        MailGroup mailGroup = mailGroupService.findMailGroupById(sendInfoForm.getSendInfoId());
        if (isNullCheck(bindingResult, sendInfoById, "sendInfo") || !sendInfoById.getStatus().equals(Status.WAIT)
                || isNullCheck(bindingResult, mailInfo, "mailInfoId")
                || isNullCheck(bindingResult, mailGroup, "mailGroupId")) {
            return "sendInfo/editSendInfo";
        }

        SendInfo sendInfo = mapToSendInfo(sendInfoForm, mailInfo, mailGroup);
        sendInfoService.updateSendInfo(sendInfoId, sendInfo);

        return "redirect:/sendInfo/{sendInfoId}";
    }



    private SendInfo mapToSendInfo(SendInfoForm sendInfoForm, MailInfo mailInfo, MailGroup mailGroup) {
        SendInfo sendInfo = modelMapper.map(sendInfoForm, SendInfo.class);
        sendInfo.setMailInfo(mailInfo);
        sendInfo.setMailGroup(mailGroup);
        sendInfo.setStatus(Status.WAIT);
        return sendInfo;
    }

    private SendInfoForm mapToSendInfoForm(SendInfo sendInfo) {
        SendInfoForm sendInfoForm = modelMapper.map(sendInfo, SendInfoForm.class);
        sendInfoForm.setMailInfoId(sendInfo.getMailInfo().getId());
        sendInfoForm.setMailGroupId(sendInfo.getMailGroup().getId());
        sendInfoForm.setStatus(Status.WAIT);
        return sendInfoForm;
    }

    private boolean isNullCheck(BindingResult bindingResult, Object o, String field) {
        if (o == null) {
            bindingResult.rejectValue(field, "field-error", "잘못된 데이터 입니다.");
            return true;
        }
        return false;
    }

}
