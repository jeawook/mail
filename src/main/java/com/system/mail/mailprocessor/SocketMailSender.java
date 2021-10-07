package com.system.mail.mailprocessor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

@Component
@RequiredArgsConstructor
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

    public void send(MailDTO mailDTO) {
        try {
            if (isConnect(mailDTO.getDomain())) {
                sendMessage(createMessage(SMTP.HELO, mailDTO.getHelo()), SMTPCode.SUCCESS);
                sendMessage(createMessage(SMTP.MAILFROM, mailDTO.getMailFrom()), SMTPCode.SUCCESS);
                sendMessage(createMessage(SMTP.RECPTO, mailDTO.getRcpTo()), SMTPCode.SUCCESS);
                sendMessage(SMTP.DATA.getValue(), SMTPCode.PROCESS);
                sendMessage(mailDTO.getData());
                String result = sendMessage(SMTP.DOT.getValue(), SMTPCode.SUCCESS);
                sendMessage(SMTP.QUIT.getValue(), SMTPCode.SERVER_CLOSE);
            }
        } catch (SMTPException e) {

        } catch (IOException e) {

        }
    }
    private boolean isConnect(String domain) throws IOException, SMTPException {

        setConnection(dnsLookup.lookup(domain));
        String code = getResultCode(getMessage());
        if (isCheckResult(code, SMTPCode.GREETING)) {
            return false;
        }
        return true;
    }
    private boolean isCheckResult(String code, SMTPCode smtpCode) {
        if (!code.equals(smtpCode.getValue())) {
            return false;
        }
        return true;
    }

    private void setConnection(String mxAddress) throws IOException {
        smtp = new Socket(mxAddress, PORT);
        smtp.setSoTimeout(SOCKET_TIME_OUT);
        input = new BufferedReader(new InputStreamReader(smtp.getInputStream()));
        output = new PrintStream(smtp.getOutputStream());
    }

    private String getMessage() throws IOException, SMTPException {
        return input.readLine();
    }

    private void sendMessage(String message) throws IOException, SMTPException {
        output.print(message + LINE_FEED_CHAR);
    }

    private String sendMessage(String message, SMTPCode smtpCode) throws IOException, SMTPException {
        sendMessage(message);
        String resultMessage = getMessage();
        if (isCheckResult(getResultCode(resultMessage), smtpCode)) {
            throw new SMTPException("Smtp protocol Exception ", resultMessage);
        }
        return resultMessage;
    }

    private String createMessage(SMTP command, String message) {
        return command.getValue() + message;
    }

    private String getResultCode(String message) throws SMTPException{
        if(message.length() < 3) {
            throw new SMTPException("Smtp protocol Exception ", message);
        }
        return message.substring(0, 3);
    }
}
