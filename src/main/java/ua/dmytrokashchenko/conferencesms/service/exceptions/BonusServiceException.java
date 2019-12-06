package ua.dmytrokashchenko.conferencesms.service.exceptions;

public class BonusServiceException extends RuntimeException {
    public BonusServiceException() {
        super();
    }

    public BonusServiceException(String message) {
        super(message);
    }

    public BonusServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BonusServiceException(Throwable cause) {
        super(cause);
    }
}
