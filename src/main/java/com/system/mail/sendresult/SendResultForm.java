package com.system.mail.sendresult;

import com.system.mail.sendresultdetail.SendResultDetailForm;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
public class SendResultForm {

    private Long sendResultId;

    private int totalCnt;

    private int errorCnt;

    private int successCnt;

    private int completeCnt;

    private ArrayList<SendResultDetailForm> sendResultDetailForms;
}
