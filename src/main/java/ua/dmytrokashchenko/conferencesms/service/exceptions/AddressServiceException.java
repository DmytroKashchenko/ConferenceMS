package ua.dmytrokashchenko.conferencesms.service.exceptions;

public class AddressServiceException extends RuntimeException {
    public AddressServiceException() {
        super();
    }

    public AddressServiceException(String message) {
        super(message);
    }

    public AddressServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressServiceException(Throwable cause) {
        super(cause);
    }
}
