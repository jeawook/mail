package com.system.mail.sendresultdetail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendResultDetailSearchCond {

    private String value;
    private ReusltSearchType type;

}
