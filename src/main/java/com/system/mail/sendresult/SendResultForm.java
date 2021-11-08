package com.system.mail.sendresult;

import com.system.mail.sendresultdetail.SendResultDetailForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SendResultForm {

    private Long sendResultId;

    private int totalCnt;

    private int errorCnt;

    private int successCnt;

    private int completedCnt;

    private String macroKey;

    private List<SendResultDetailForm> sendResultDetails;
}
