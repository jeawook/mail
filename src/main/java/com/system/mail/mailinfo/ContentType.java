package com.system.mail.mailinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContentType {
    HTML("text/html"),PLAIN("text/plain");

    private String contentType;
}
