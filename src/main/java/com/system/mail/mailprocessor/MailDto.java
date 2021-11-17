package com.system.mail.mailprocessor;

import com.system.mail.common.MailAddress;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "MailDtoBuilder")
@Getter @Setter
public class MailDto {

    private MailAddress rcpTo;

    private MailAddress mailFrom;

    private String data;

    public String getRcpToDomain() {
        return rcpTo.getDomain();
    }
    public String getRcpTo() {
        return rcpTo.getAddress();
    }
    public String getMailFromAddress() {
        return mailFrom.getAddress();
    }
    public static MailDtoBuilder builder(MailAddress rcpTo, MailAddress mailFrom, String data) {
        return MailDtoBuilder().rcpTo(rcpTo).mailFrom(mailFrom).data(data);
    }
}
