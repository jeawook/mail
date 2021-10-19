package com.system.mail.common;


import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Getter
public class MailAddress {

    private String name;

    @Email
    private String email;

    @Builder(builderMethodName = "MailAddressBuilder")
    public MailAddress(@NonNull String name,@NonNull @Email String email) {
        this.name = name;
        this.email = email;
    }

    public static MailAddressBuilder builder(String name, String email) {
        return MailAddressBuilder().name(name).email(email);
    }

    public String getDomain() {
        int index = email.indexOf("@");
        return email.substring(index+1);
    }

    public String getHeaderAddress() {
        return "\"" + name + "\""+getAddress() ;
    }

    public String getAddress() {
        return "<"+email+">";
    }
}
