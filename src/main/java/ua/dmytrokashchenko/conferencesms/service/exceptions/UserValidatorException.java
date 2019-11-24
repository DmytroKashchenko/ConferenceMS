package ua.dmytrokashchenko.conferencesms.service.exceptions;

public class UserValidatorException extends RuntimeException {
    public UserValidatorException() {
        super();
    }

    public UserValidatorException(String message) {
        super(message);
    }

    public UserValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserValidatorException(Throwable cause) {
        super(cause);
    }
}
