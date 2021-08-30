package com.system.mail.sendresult;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SendResultDetail {

    @Id @GeneratedValue
    @Column(name = "send_result_detail_id")
    private Long id;
}
