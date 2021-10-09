package com.system.mail.mailprocessor;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@ConfigurationProperties(prefix = "domain")
public class DomainConnectionProperties {
    /**
     * 발송 도메인 개수 정보
     */
    private HashMap<String, Integer> domainConnectionInfo = new HashMap<>();
    private static int defaultDomainConn;

    public int getConnection(String domain) {

        if (domainConnectionInfo.containsKey(domain)) {
            return domainConnectionInfo.get(domain);
        }
        addInfo(domain);

        return defaultDomainConn;
    }
    private void addInfo(String domain) {
        domainConnectionInfo.put(domain, defaultDomainConn);
    }
}
