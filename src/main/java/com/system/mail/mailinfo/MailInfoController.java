package com.system.mail.mailinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mailInfo")
@RequiredArgsConstructor
public class MailInfoController {

    private final MailInfoService mailInfoService;

    @GetMapping
    public Page mailInfoList(@PageableDefault(size = 10, sort = "id") Pageable pageable, Model model) {
        Page<MailInfo> mailInfoList = mailInfoService.findMailInfoList(pageable);
    }
}
