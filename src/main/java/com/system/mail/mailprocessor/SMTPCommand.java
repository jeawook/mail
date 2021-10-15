package com.system.mail.mailprocessor;

public enum SMTPCommand {
    HELO("helo "), MAILFROM("mail from:"), RECPTO("rcpt to:"), DATA("data"), DOT("."), QUIT("quit");

    private String value;

    SMTPCommand(String value) {
        this.value = value;
    }
    public String getName() {
        return name();
    }
    public String getValue() {
        return value;
    }
}
