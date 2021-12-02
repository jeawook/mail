package com.system.mail.sendresultdetail;

import lombok.Builder;
import lombok.Data;

@Data
public class SendResultDetailSearchCond {

    private String searchKey;
    private ResultSearchType searchType;

    @Builder
    public SendResultDetailSearchCond(String searchKey, ResultSearchType searchType) {
        this.searchKey = searchKey;
        this.searchType = searchType;
    }
}
