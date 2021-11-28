package com.system.mail.sendresult;

import com.system.mail.sendresultdetail.SendResultDetail;
import com.system.mail.sendresultdetail.SendResultDetailSearchCond;
import com.system.mail.sendresultdetail.SendResultDetailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/result")
public class SendResultController {

    private final SendResultService sendResultService;
    private final SendResultDetailService sendResultDetailService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public String sendResult(@PathVariable Long id, @PageableDefault Pageable pageable, Model model) {

        SendResult sendResultById = sendResultService.findById(id);
        SendResultForm sendResultForm = modelMapper.map(sendResultById, SendResultForm.class);

        Page<SendResultDetail> bySendResultId = sendResultDetailService.findBySendResultId(id, pageable);

        model.addAttribute("sendResult", sendResultForm);
        model.addAttribute("sendResultDetails", bySendResultId);
        return "sendResult/sendResult";
    }

    @GetMapping("/{id}/search")
    public String searchSendResult(@PathVariable Long id, SendResultDetailSearchCond searchCond,@PageableDefault Pageable pageable, Model model) {

        SendResult sendResultById = sendResultService.findById(id);
        Page<SendResultDetail> bySendResultId = sendResultDetailService.findByNameOrEmail(id, searchCond, pageable);
        SendResultForm sendResultForm = modelMapper.map(sendResultById, SendResultForm.class);

        model.addAttribute("sendResult", sendResultForm);
        model.addAttribute("sendResultDetails", bySendResultId);
        return "sendResult/sendResult";
    }

}
