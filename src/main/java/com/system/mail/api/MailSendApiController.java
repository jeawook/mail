package com.system.mail.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailSendApiController {

    private final MailSendApiService mailSendApiService;

    @PostMapping("/send")
    public ResponseEntity<MailSendResponse> send(@Valid @RequestBody MailSendRequest request) {
        MailSendResponse response = mailSendApiService.send(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        String message = e.getMessage() != null ? e.getMessage() : "잘못된 요청입니다.";
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }
}
