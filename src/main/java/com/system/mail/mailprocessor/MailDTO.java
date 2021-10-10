package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "MailDTOBuilder")
@Getter
public class MailDTO {

    private Long resultDetailId;

    private MailAddress rcpTo;

    private MailAddress mailFrom;

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
    public static MailDTOBuilder mailDto(Long resultDetailId, String helo, MailAddress rcpTo, MailAddress mailFrom, String data) {
        return MailDTOBuilder().resultDetailId(resultDetailId)
                .rcpTo(rcpTo)
                .mailFrom(mailFrom)
                .data(data);
    }
}
