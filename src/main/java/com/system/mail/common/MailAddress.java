package com.system.mail.common;


import com.mysema.commons.lang.Assert;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public MailAddress(@Length(max = 20, message = "이름은 최대 20자") String name, @Email @NotNull String email) {
        Assert.notNull(name, "name must not be null");
        Assert.notNull(email, "email must not be null");
        this.name = name;
        this.email = email;
    }

}
