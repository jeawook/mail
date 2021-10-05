package com.system.mail.mailprocessor;


public class SmtpCode {

    public static final String GREETING = "220";
    public static final String SERVER_CLOSE = "221";
    public static final String SUCCESS = "250";

    public static final String PROCESS = "354";

    public static final String MAIL_BOX_ERROR = "450";

    public static final String MAIL_BOX_FULL = "552";
    public static final String NO_SUCH_USER = "550";

    public static final String SERVER_ERROR = "530";
}
