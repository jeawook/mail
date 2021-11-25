package com.system.mail.mailprocessor;

import com.mysema.commons.lang.Assert;
import com.system.mail.common.MailAddress;
import lombok.*;

import static com.mysema.commons.lang.Assert.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "MailDtoBuilder")
@Getter @Setter
public class MailDto {

    private MailAddress rcpTo;

    private MailAddress mailFrom;

    private String data;

    public String getRcpToDomain() {
        return rcpTo.getDomain();
    }
    public String getRcpToAddress() {
        return rcpTo.getAddress();
    }
    public String getMailFromAddress() {
        return mailFrom.getAddress();
    }

    @Builder
    public MailDto(MailAddress rcpTo, MailAddress mailFrom, String data) {
        notNull(rcpTo, "rcpTo must not be null");
        notNull(mailFrom, "mailFrom must not be null");
        notNull(data, "data must not be null");
        this.rcpTo = rcpTo;
        this.mailFrom = mailFrom;
        this.data = data;
    }
}
