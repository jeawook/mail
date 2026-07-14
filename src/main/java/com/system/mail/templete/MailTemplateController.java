package com.system.mail.templete;

import com.system.mail.mailprocessor.MacroProcessor;
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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/template")
@RequiredArgsConstructor
public class MailTemplateController {

    private final MailTemplateService mailTemplateService;
    private final ModelMapper modelMapper;
    private final MacroProcessor macroProcessor;

    @GetMapping("/list")
    public String mailTemplateList(@PageableDefault(size = 10, sort = "id") Pageable pageable, Model model) {
        Page<MailTemplate> mailTemplateList = mailTemplateService.findMailTemplateListByPage(pageable);
        model.addAttribute("mailTemplateList", mailTemplateList);
        return "templete/mailTemplateList";
    }

    @GetMapping("/list/search")
    public String searchMailTemplate(Pageable pageable, @RequestParam String searchKey, Model model) {
        Page<MailTemplate> mailTemplateByName = mailTemplateService.findMailTemplateByName(searchKey, pageable);
        model.addAttribute("mailTemplateList", mailTemplateByName);
        return "templete/mailTemplateList";
    }

    @GetMapping("/{mailTemplateId}")
    public String mailTemplate(@PathVariable Long mailTemplateId, Model model) {
        MailTemplate mailTemplate = mailTemplateService.findById(mailTemplateId);
        model.addAttribute("mailTemplate", mailTemplate);
        return "templete/mailTemplate";
    }

    @GetMapping("/{mailTemplateId}/edit")
    public String editForm(@PathVariable Long mailTemplateId, Model model) {
        MailTemplate mailTemplate = mailTemplateService.findById(mailTemplateId);
        model.addAttribute("mailTemplate", mailTemplate);
        return "templete/editMailTemplate";
    }

    @PostMapping("/{mailTemplateId}/edit")
    public String edit(@PathVariable Long mailTemplateId,
                       @Validated @ModelAttribute(name = "mailTemplate") MailTemplateForm mailTemplateForm,
                       BindingResult bindingResult) {
        checkMacroValidation(mailTemplateForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "templete/editMailTemplate";
        }
        MailTemplate mailTemplate = modelMapper.map(mailTemplateForm, MailTemplate.class);
        mailTemplateService.updateMailTemplate(mailTemplateId, mailTemplate);
        return "redirect:/template/{mailTemplateId}";
    }

    @GetMapping("/add")
    public String createForm(@ModelAttribute(name = "mailTemplate") MailTemplateForm mailTemplateForm) {
        return "templete/createMailTemplate";
    }

    @PostMapping("/add")
    public String create(@Validated @ModelAttribute(name = "mailTemplate") MailTemplateForm mailTemplateForm,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        checkMacroValidation(mailTemplateForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "templete/createMailTemplate";
        }
        MailTemplate mailTemplate = modelMapper.map(mailTemplateForm, MailTemplate.class);
        MailTemplate saved = mailTemplateService.saveMailTemplate(mailTemplate);

        redirectAttributes.addAttribute("mailTemplateId", saved.getId());
        return "redirect:/template/{mailTemplateId}";
    }

    /**
     * subject/content 에 실제로 쓰인 [$key$] 플레이스홀더 목록과 macroKey 가 정확히 일치하는지 검증한다.
     * 하나라도 다르면(추가/누락/오타) 등록·수정을 막아, 실제 발송 시 치환되지 않은 [$key$]가
     * 그대로 노출되거나 macroValue 개수 검증(MailGroupController, MailSendApiService)이 어긋나는 것을 방지한다.
     */
    private void checkMacroValidation(MailTemplateForm mailTemplateForm, BindingResult bindingResult) {
        Set<String> usedKeys = new LinkedHashSet<>();
        usedKeys.addAll(macroProcessor.extractKeys(mailTemplateForm.getSubject()));
        usedKeys.addAll(macroProcessor.extractKeys(mailTemplateForm.getContent()));

        Set<String> declaredKeys = parseMacroKey(mailTemplateForm.getMacroKey());

        if (!usedKeys.equals(declaredKeys)) {
            bindingResult.reject("macroKeyMismatch",
                    "macroKey 는 제목/본문에 사용된 [$key$] 플레이스홀더와 정확히 일치해야 합니다.");
        }
    }

    private Set<String> parseMacroKey(String macroKey) {
        if (macroKey == null || macroKey.trim().isEmpty()) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(macroKey.split(","))
                .map(String::trim)
                .filter(key -> !key.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 템플릿 기반 API 발송 테스트를 위한 샘플 데이터
     */
    @PostConstruct
    public void init() {
        MailTemplate mailTemplate = MailTemplate.builder()
                .templateName("가입 환영 템플릿")
                .subject("[$name$]님, 가입을 환영합니다")
                .content("안녕하세요 [$name$]님,\n회원가입을 환영합니다. 등급: [$grade$]")
                .macroKey("name,grade")
                .build();
        mailTemplateService.saveMailTemplate(mailTemplate);
    }
}
