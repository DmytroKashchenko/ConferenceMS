package ua.dmytrokashchenko.conferencesms.service.exceptions;

public class EventValidatorException extends RuntimeException {
    public EventValidatorException() {
        super();
    }

    public EventValidatorException(String message) {
        super(message);
    }

    public EventValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventValidatorException(Throwable cause) {
        super(cause);
    }
}
