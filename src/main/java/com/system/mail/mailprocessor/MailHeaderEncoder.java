package com.system.mail.mailprocessor;


import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static java.util.Base64.Encoder;

@Component
public class MailHeaderEncoder {
    public static final String CRLF = "\r\n";

    public static final String HEADER_RETURN_PATH = "Return-Path";

    public static final String HEADER_MESSAGE_ID = "Message-ID";

    public static final String HEADER_FROM = "From";

    public static final String HEADER_TO = "To";

    public static final String HEADER_SUBJECT = "Subject";

    public static final String HEADER_DATE = "Date";

    public static final String HEADER_MIME_VERSION = "MIME-Version";

    public static final String HEADER_REPLY_TO = "Reply-To";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    public static final String HEADER_CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";

    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    public static final String HEADER_X_MAILER = "X-Mailer";

    public static final String HEADER_X_TEST = "X-Test";

    public static final String HEADER_X_EMAIL = "X-Email";

    public static final String HEADER_DKIM = "DKIM-Signature";

    public static String create(String key, String value) {
        return String.format("%s: %s%s", key, value, "\r\n");
    }

    public static String encodeNameHeader(String value, String charset) {
        Encoder encoder = Base64.getEncoder();
        String encoded;
        int begin;
        int end;
        String name;
        String tempStr;
        try {
            begin = value.indexOf("\"");
            end = value.lastIndexOf("\"");
            if (begin != -1 && end != -1) {
                name = value.substring(begin + 1, end);
                name = name.trim();
                if (name.length() > 0) {
                    try {
                        tempStr = new String(encoder.encode(name.getBytes(charset)));
                    } catch (UnsupportedEncodingException e) {
                        tempStr = new String(encoder.encode(name.getBytes()));
                    }
                    encoded = String.format("=?%s?B?%s?=%s", charset, tempStr, value.substring(end + 1));
                } else {
                    encoded = value.substring(end + 1).trim();
                }
            } else {
                encoded = value;
            }
        } catch (Exception e) {
            encoded = value;
        }
        return encoded;
    }

    public static String encodeHeader(String subject, String charset) {
        return encodeHeader(subject, charset, 40);
    }

    public static String encodeHeader(String subject, String charset, int limit) {
        Encoder encoder = Base64.getEncoder();
        byte[] buffer;
        String part;
        String tempStr;
        int block = 0;
        int begin = 0;
        int end = 0;
        int subjectLen = 0;
        StringBuffer sb = new StringBuffer();
        subjectLen = subject.length();
        block = subjectLen / limit;
        if (subjectLen % limit > 0)
            block++;
        for (int i = 0; i < block; i++) {
            end = begin + limit;
            if (end > subjectLen)
                end = subjectLen;
            part = subject.substring(begin, end);
            try {
                buffer = part.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                buffer = part.getBytes();
            }
            tempStr = new String(encoder.encode(buffer));
            if (i == 0) {
                sb.append("=?").append(charset).append("?B?").append(tempStr).append("?=");
            } else {
                sb.append("\r\n\t=?").append(charset).append("?B?").append(tempStr).append("?=");
            }
            begin = end;
        }
        return sb.toString();
    }
}
