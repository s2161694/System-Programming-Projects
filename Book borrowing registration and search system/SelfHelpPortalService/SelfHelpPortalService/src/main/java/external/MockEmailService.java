package external;

import java.util.regex.Pattern;

/**
 * A mock Email Service Provider implementation for testing.
 * This does not actually send any emails, just prints email data to standard output.
 */
public class MockEmailService implements EmailService {
    // Color codes from https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_RESET = "\u001B[0m";
    // From https://owasp.org/www-community/OWASP_Validation_Regex_Repository
    private static final Pattern OWASP_EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    @Override
    public int sendEmail(String sender, String recipient, String subject, String content) {
        if (sender == null || !OWASP_EMAIL_PATTERN.matcher(sender).matches()) {
            return STATUS_INVALID_SENDER_EMAIL;
        }
        if (recipient == null || !OWASP_EMAIL_PATTERN.matcher(recipient).matches()) {
            return STATUS_INVALID_RECIPIENT_EMAIL;
        }
        System.out.print(ANSI_CYAN);
        System.out.println("Email from " + sender + " to " + recipient);
        System.out.println(subject);
        System.out.println(content);
        System.out.print(ANSI_RESET);
        return STATUS_SUCCESS;
    }
}
