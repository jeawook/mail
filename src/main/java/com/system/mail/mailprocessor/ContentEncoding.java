package com.system.mail.mailprocessor;

public enum ContentEncoding {
    DEFAULT("Quoted-Printable"), BASE64("base64");

    private String value;

    ContentEncoding(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
