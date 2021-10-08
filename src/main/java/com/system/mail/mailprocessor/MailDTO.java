package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "MailDTOBuilder")
@Getter
public class MailDTO {

    private Long resultDetailId;

    private String helo;

    private MailAddress mailAddress;

    private String mailFrom;

    private String rcpTo;

    private String data;

    public String getDomain() {
        return mailAddress.getDomain();
    }
    public static MailDTOBuilder mailDto(Long resultDetailId, String helo, MailAddress mailAddress, String mailFrom, String rcpTo, String data) {
        return MailDTOBuilder().resultDetailId(resultDetailId)
                .mailAddress(mailAddress)
                .mailFrom(mailFrom)
                .rcpTo(rcpTo)
                .data(data);
    }
}
