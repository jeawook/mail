package com.system.mail.templete;

import com.system.mail.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MailTemplate extends BaseTimeEntity {

    @GeneratedValue @Id
    private Long id;

    private String subject;

    private String content;
}
