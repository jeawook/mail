package com.system.mail.common;


import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

import static java.util.regex.Pattern.matches;

@Embeddable
@EqualsAndHashCode
@Builder
public class MailAddress {

    @Email
    private String address;

    private String name;

    @Override
    public String toString() {
        return "\""+name+"\"<"+address+">";
    }
}
