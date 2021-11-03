package com.system.mail.mailprocessor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Set;

@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "mail")
@Setter
public class MailProperties {

    private HashMap<String, String> propertyMap = new HashMap<>();
    private String domain;

    public String getProperty(String key) {
        String value = propertyMap.get(key);
        return value != null ? value : "";
    }
    public Set<String> getProperties() {
        return propertyMap.keySet();
    }
    public String getDomain() {
        return domain;
    }
}
