package com.system.mail.mailinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ContentType {
    HTML("text/html"),PLAIN("text/plain");

    private String contentType;

    public String getValue() {
        return contentType;
    }
    public String getName() {
        return this.name();
    }
}
