package com.system.mail.mailprocessor;

import com.system.mail.sendresultdetail.SendResultDetail;
import com.system.mail.sendresultdetail.SendResultDetailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final SendResultDetailService sendResultDetailService;

    @Transactional
    public void send(MailDTO mailDTO) {
        SendResultDetail sendResultDetail = sendResultDetailService.findById(mailDTO.getResultDetailId());
        String resultMessage;
        String resultCode;
        try {
            if (isConnect(mailDTO.getToDomain())) {
                sendMessage(createMessage(SMTP.HELO, SERVER_DOMAIN), SMTPCode.SUCCESS);
                sendMessage(createMessage(SMTP.MAILFROM, mailDTO.getMailFrom()), SMTPCode.SUCCESS);
                sendMessage(createMessage(SMTP.RECPTO, mailDTO.getRcpTo()), SMTPCode.SUCCESS);
                sendMessage(SMTP.DATA.getValue(), SMTPCode.PROCESS);
                sendMessage(mailDTO.getData());
                resultMessage = sendMessage(SMTP.DOT.getValue(), SMTPCode.SUCCESS);
                sendMessage(SMTP.QUIT.getValue(), SMTPCode.SERVER_CLOSE);
                resultCode = SMTPCode.SUCCESS.getValue();
            } else {
                resultCode = SMTPCode.SERVER_ERROR.name();
                resultMessage = SMTPCode.SERVER_ERROR.getValue();
            }
        } catch (SMTPException e) {
            resultMessage = e.getMessage();
            resultCode = e.getCode();
        } catch (IOException e) {
            resultCode = SMTPCode.SYSTEM_ERROR.getValue();
            resultMessage = SMTPCode.SERVER_ERROR.name();
        } catch (Exception e) {
            resultCode = SMTPCode.SYSTEM_ERROR.getValue();
            resultMessage = SMTPCode.SYSTEM_ERROR.name();
        } finally {
            quit();
        }
        sendResultDetail.setResult(resultCode, resultMessage);
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
            throw new SMTPException("Smtp protocol Exception : "+message, SMTPCode.SERVER_ERROR.getValue());
        }
        return message.substring(0, 3);
    }

    private void quit(){
        try{
            input.close();
            output.flush();
            output.close();
            smtp.close();
        }catch(Exception e){
            logger.trace("connection close Error :  " + e.getMessage());
        }
    }
}
