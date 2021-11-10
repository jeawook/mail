package com.system.mail.common;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "MailAddressBuilder")
@Getter
public class MailAddress {

    @Length(max = 20,message = "이름은 최대 20자")
    private String name;

    @Email
    @NotNull
    private String email;

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

    public static MailAddressBuilder builder(String name, String email) {
        return MailAddressBuilder()
                .name(name)
                .email(email);
    }


}
