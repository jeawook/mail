package com.system.mail.converter;

import com.system.mail.common.MailAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToMailAddress implements Converter<String, MailAddress> {

    @Override
    public MailAddress convert(String source) {
        log.info("mailAddress:{}",source);
        String[] tmp = source.split(",");
        return MailAddress.builder().name(tmp[0]).email(tmp[1]).build();
    }
}
