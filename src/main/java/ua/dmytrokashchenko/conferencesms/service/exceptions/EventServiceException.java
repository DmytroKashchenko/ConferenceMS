package ua.dmytrokashchenko.conferencesms.service.exceptions;

public class EventServiceException extends RuntimeException {
    public EventServiceException() {
        super();
    }

    public EventServiceException(String message) {
        super(message);
    }

    public EventServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventServiceException(Throwable cause) {
        super(cause);
    }
}
