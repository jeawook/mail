package com.system.mail.mailprocessor;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

@Component
@AllArgsConstructor
public class SocketMailSender {

    private Socket smtp;
    private BufferedReader input;
    private PrintStream output;
    private static final int SOCKET_TIME_OUT = 10000;
    private static final String SERVER_DOMAIN = "sender.com";
    private static final int PORT = 25;
    private static final String LINE_FEED_CHAR = "\r\n";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DNSLookup dnsLookup;

    private boolean isConnect(String domain) throws IOException, SMTPException {
        String lookup = dnsLookup.lookup(domain);

        setSocket(lookup);

        String code = getCode(getMessage());
        if (!code.equals(SmtpCode.GREETING)) {
            return false;
        }
        return true;
    }

    private void setSocket(String lookup) throws IOException {
        smtp = new Socket(lookup, PORT);
        smtp.setSoTimeout(SOCKET_TIME_OUT);
        input = new BufferedReader(new InputStreamReader(smtp.getInputStream()));
    }

    private String getMessage() throws IOException, SMTPException {
        return getCode(input.readLine());
    }

    private String sendMessage(String message) throws IOException, SMTPException {
        output.print(message);
        return getMessage();
    }

    private String getCode(String message) throws SMTPException{
        if(message.length() < 3) {
            throw new SMTPException("Smtp protocol Exception ", SmtpCode.SERVER_ERROR);
        }
        return message.substring(0, 3);
    }
}
