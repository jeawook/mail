package com.system.mail.mailprocessor;

public enum SMTPCode {
    GREETING("220"), SERVER_CLOSE("221"), SUCCESS("250"), PROCESS("354"), MAIL_BOX_ERROR("450"),
    MAIL_BOX_FULL("550"), NO_SUCH_USER("550"), SERVER_ERROR("530"), SYSTEM_ERROR("-1");

    private String value;

    SMTPCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
