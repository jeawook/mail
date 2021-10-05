package com.system.mail.mailprocessor;

public class SMTPException extends Exception
{
    private String message;
    private String code;

    public SMTPException(String message, String code){
        this.message = message;
        this.code = code;
    }

    public String getMessage(){
        return message;
    }

    public String getCode() { return code; }
}
