package com.system.mail.sendresultdetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class SendResultDetailSearchCond {

    private String value;
    private ReusltSearchType type;

    @Builder
    public SendResultDetailSearchCond(String value, ReusltSearchType type) {
        this.value = value;
        this.type = type;
    }
}
