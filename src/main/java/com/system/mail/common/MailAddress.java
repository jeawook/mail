package com.system.mail.common;


import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class MailAddress {

    @Email
    private String email;

    private String name;

    @Override
    public String toString() {
        return "\""+name+"\"<"+ email +">";
    }
}
