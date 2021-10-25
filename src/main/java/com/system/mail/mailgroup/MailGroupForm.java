package com.system.mail.mailgroup;

import com.system.mail.user.UserForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class MailGroupForm {

    @NotBlank
    private String mailGroupName;

    private String macroKey;

    ArrayList<UserForm> users;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
