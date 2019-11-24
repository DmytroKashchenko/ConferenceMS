package ua.dmytrokashchenko.conferencesms.service.validator;

public interface Validator<E> {
    void validate(E entity);
}
