package com.system.mail.converter;

import com.system.mail.common.MailAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class MailAddressToString implements Converter<MailAddress, String> {

    @Override
    public String convert(MailAddress source) {
        return source.getName()+","+source.getEmail();
    }
}
