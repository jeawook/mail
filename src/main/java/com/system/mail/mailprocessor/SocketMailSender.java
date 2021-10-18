package com.system.mail.mailprocessor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
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

    public SMTPResult send(MailDTO mailDTO) {
        String resultMessage;
        String resultCode;
        try {
            if (isConnect(mailDTO.getToDomain())) {
                sendMessage(createMessage(SMTPCommand.HELO, SERVER_DOMAIN), SMTPCode.SUCCESS);
                sendMessage(createMessage(SMTPCommand.MAILFROM, mailDTO.getMailFrom()), SMTPCode.SUCCESS);
                sendMessage(createMessage(SMTPCommand.RECPTO, mailDTO.getRcpTo()), SMTPCode.SUCCESS);
                sendMessage(SMTPCommand.DATA.getValue(), SMTPCode.PROCESS);
                sendMessage(mailDTO.getData());
                resultMessage = sendMessage(SMTPCommand.DOT.getValue(), SMTPCode.SUCCESS);
                sendMessage(SMTPCommand.QUIT.getValue(), SMTPCode.SERVER_CLOSE);
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
        return SMTPResult.builder(resultCode, resultMessage).build();
    }
    private boolean isConnect(String domain) throws IOException, SMTPException, NamingException {
        logger.info("lookup domain : "+ domain);
        setConnection(dnsLookup.lookup(domain));
        String code = getResultCode(getMessage());
        if (isCheckResult(code, SMTPCode.GREETING)) {
            return false;
        }
        return true;
    }
    private boolean isCheckResult(String code, SMTPCode smtpCode) {
        if (code.equals(smtpCode.getValue())) {
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

    private String getMessage() throws IOException{
        return input.readLine();
    }

    private void sendMessage(String message){
        output.print(message + LINE_FEED_CHAR);
        logger.info("send message : "+message);
    }

    private String sendMessage(String message, SMTPCode smtpCode) throws IOException, SMTPException {
        sendMessage(message);
        String resultMessage = getMessage();
        String resultCode = getResultCode(resultMessage);
        if (isCheckResult(resultCode, smtpCode)) {
            throw new SMTPException("Smtp protocol Exception " + resultMessage, resultCode );
        }
        return resultMessage;
    }

    private String createMessage(SMTPCommand command, String message) {
        return command.getValue() + message;
    }

    private String getResultCode(String message) throws SMTPException{
        logger.info("get message : "+message);
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
        logger.info("quit connection");
    }
}
