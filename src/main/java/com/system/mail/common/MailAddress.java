package com.system.mail.common;


import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "MailAddressBuilder")
@Getter
public class MailAddress {

    private String name;

    private String email;

    public static MailAddressBuilder builder(String name, String email) {
        return MailAddressBuilder().name(name).email(email);
    }


    /*@Override
    public String toString() {
        return "\""+name+"\"<"+ email +">";
    }*/
}
