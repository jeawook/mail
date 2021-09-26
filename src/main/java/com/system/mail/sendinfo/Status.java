package com.system.mail.sendinfo;

public enum Status {
    REGISTER("등록"),
    WAIT("발송대기"),
    SENDING("발송중"),
    COMPLETE("발송완료");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }
    public String getValue() {
        return value;
    }
}
