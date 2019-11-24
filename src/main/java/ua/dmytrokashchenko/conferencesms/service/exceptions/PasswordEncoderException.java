package ua.dmytrokashchenko.conferencesms.service.exceptions;

public class PasswordEncoderException extends RuntimeException {

    public PasswordEncoderException() {
        super();
    }

    public PasswordEncoderException(String message) {
        super(message);
    }

    public PasswordEncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordEncoderException(Throwable cause) {
        super(cause);
    }
}
