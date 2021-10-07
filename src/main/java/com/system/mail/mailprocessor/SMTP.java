package com.system.mail.mailprocessor;

public enum SMTP {
    HELO("helo"), MAILFROM("mail from:"), RECPTO("recp to:"), DATA("data"), DOT("."), QUIT("quit");

    private String value;

    SMTP(String value) {
        this.value = value;
    }
    public String getName() {
        return name();
    }
    public String getValue() {
        return value;
    }
}
