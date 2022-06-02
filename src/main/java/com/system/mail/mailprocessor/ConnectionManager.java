package com.system.mail.mailprocessor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class ConnectionManager {

    private final HashMap<String, Integer> connectionList = new HashMap<>();
    private final DomainConnectionProperties domainConnProperties;

    private Boolean isCheckConn(String conn) {
        return domainConnProperties.getConnection(conn) > getConnCnt(conn);
    }

    private Integer getConnCnt(String conn) {
        return connectionList.getOrDefault(conn, 0);
    }

    public Boolean addConn(String conn) {
        if (isCheckConn(conn)) {
            setConn(conn, 1);
            return true;
        }
        return false;
    }

    public void removeConn(String conn) {
        Integer cnt = getConnCnt(conn);
        setConn(conn, -1);
    }

    private void setConn(String conn, Integer flag) {
        connectionList.put(conn, getConnCnt(conn) + flag);
    }

}
