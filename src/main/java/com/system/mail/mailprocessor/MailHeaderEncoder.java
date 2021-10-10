package com.system.mail.mailprocessor;


import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static java.util.Base64.Encoder;

@Component
public class MailHeaderEncoder {

    public String encode(String header, String value, String charset) {
        return encodeHeader(create(header, value), charset);
    }
    public String create(String key, String value) {
        return String.format("%s: %s%s", key, value, "\r\n");
    }

    public String encodeNameHeader(String key, String value, String charset) {
        return encodeNameHeader(create(key, value), charset);
    }

    private String encodeNameHeader(String value, String charset) {
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

    private String encodeHeader(String value, String charset) {
        return encodeHeader(value, charset, 40);
    }

    private String encodeHeader(String value, String charset, int limit) {
        Encoder encoder = Base64.getEncoder();
        byte[] buffer;
        String part;
        String tempStr;
        int block = 0;
        int begin = 0;
        int end = 0;
        int subjectLen = 0;
        StringBuffer sb = new StringBuffer();
        subjectLen = value.length();
        block = subjectLen / limit;
        if (subjectLen % limit > 0)
            block++;
        for (int i = 0; i < block; i++) {
            end = begin + limit;
            if (end > subjectLen)
                end = subjectLen;
            part = value.substring(begin, end);
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
