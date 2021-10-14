package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "MailDTOBuilder")
@Getter
public class MailDTO {

    @NotNull
    private MailAddress rcpTo;

    @NotNull
    private MailAddress mailFrom;

    @NotNull
    private String data;

    public String getToDomain() {
        return rcpTo.getDomain();
    }
    public String getRcpTo() {
        return rcpTo.getAddress();
    }
    public String getMailFrom() {
        return mailFrom.getAddress();
    }
    public static MailDTOBuilder mailDto(MailAddress rcpTo, MailAddress mailFrom, String data) {
        return MailDTOBuilder()
                .rcpTo(rcpTo)
                .mailFrom(mailFrom)
                .data(data);
    }
}
