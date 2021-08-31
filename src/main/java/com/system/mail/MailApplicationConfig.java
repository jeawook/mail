package com.system.mail;

import com.system.mail.converter.MailAddressToString;
import com.system.mail.converter.StringToMailAddress;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MailApplicationConfig implements WebMvcConfigurer {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    
    // form 에서 전달 받은 string 타입의 mailAddress 정보를 MailAddress 객체로 자동 변환
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToMailAddress());
        registry.addConverter(new MailAddressToString());
    }
}
