package com.system.mail.mailprocessor;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "domain")
@Setter
public class DomainConnectionProperties {
    /**
     * 발송 도메인 개수 정보
     */
    private HashMap<String, Integer> domainConnectionInfo = new HashMap<>();
    private static final String DEFAULT_CONNECTION = "default";

    public int getConnection(String domain) {

        if (domainConnectionInfo.containsKey(domain)) {
            return domainConnectionInfo.get(domain);
        }
        return domainConnectionInfo.get(DEFAULT_CONNECTION);
    }
}
