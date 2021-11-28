package com.system.mail.sendresultdetail;

import lombok.Builder;
import lombok.Data;

@Data
public class SendResultDetailSearchCond {

    private String value;
    private ResultSearchType type;

    @Builder
    public SendResultDetailSearchCond(String value, ResultSearchType type) {
        this.value = value;
        this.type = type;
    }
}
