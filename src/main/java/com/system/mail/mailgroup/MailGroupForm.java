package com.system.mail.mailgroup;

import com.system.mail.user.UserForm;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class MailGroupForm {

    @NotBlank
    @Length(max = 255, message = "그룹명은 최대 255자")
    private String mailGroupName;

    @Length(max = 4000, message = "최대 4000자 까지 가능 합니다.")
    private String macroKey;

    @Valid
    ArrayList<UserForm> users;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
