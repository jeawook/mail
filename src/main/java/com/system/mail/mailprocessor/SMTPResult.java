package com.system.mail.mailprocessor;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class SMTPResult {
    @NotNull
    private String resultCode;
    @NotNull
    private String resultMessage;

    @Override
    public String toString() {
        return "SMTPResult{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                '}';
    }
}
