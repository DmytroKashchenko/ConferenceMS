package ua.dmytrokashchenko.conferencesms.service.exceptions;

public class PresentationServiceException extends RuntimeException {
    public PresentationServiceException() {
        super();
    }

    public PresentationServiceException(String message) {
        super(message);
    }

    public PresentationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PresentationServiceException(Throwable cause) {
        super(cause);
    }
}
