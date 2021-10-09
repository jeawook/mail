package com.system.mail.mailprocessor;

public enum MailHeader {
    CRLF("\r\n"), RETURN_PATH("Return-Path"), MESSAGE_ID("Message-ID"), FROM("From"),
    TO("To"), SUBJECT("Subject"), DATE("Date"), MIME_VERSION("MIME-Version"),
    REPLY_TO("Reply-To"), CONTENT_TYPE("Content-Type"), CONTENT_TRANSFER_ENCODING("Content-Transfer-Encoding"),
    CONTENT_DISPOSITION("Content-Disposition"), DKIM("DKIM-Signature"), CHARSET("charset");

    private String value;

    MailHeader(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public String getName() {
        return name();
    }

}
