package com.system.mail.mailprocessor;

import com.mysema.commons.lang.Assert;
import lombok.*;

import javax.validation.constraints.NotNull;

import static com.mysema.commons.lang.Assert.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class SMTPResult {
    @NotNull
    private String resultCode;
    @NotNull
    private String resultMessage;

    @Builder
    public SMTPResult(@NotNull String resultCode, @NotNull String resultMessage) {
        notNull(resultCode, "resultCode must not be null");
        notNull(resultMessage, "resultMessage must not be null");

        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }
}
