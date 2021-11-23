package com.system.mail.converter;

import com.system.mail.common.MailAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToMailAddress implements Converter<String, MailAddress> {

    @Override
    public MailAddress convert(String source) {
        log.info("mailAddress:{}",source);
        String[] tmp = source.split(",");
        if (source.indexOf(",") != 1 && tmp.length !=2 ) {
            return null;
//            throw new IllegalArgumentException("주소는 \"이름,메일\" 로 입력 되어야 합니다.");
        }
        return MailAddress.builder().name(tmp[0]).email(tmp[1]).build();
    }
}
