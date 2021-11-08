package com.system.mail.mailinfo;

public enum ContentEncoding {
    DEFAULT("quoted-printable"), BASE64("base64");

    private String value;

    ContentEncoding(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
