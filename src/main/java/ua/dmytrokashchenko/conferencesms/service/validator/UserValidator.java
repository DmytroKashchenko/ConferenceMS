package ua.dmytrokashchenko.conferencesms.service.validator;

import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.service.exceptions.UserValidatorException;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator<User> {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
    private static final Pattern NAME_PATTERN =
            Pattern.compile("(\\p{Upper}(\\p{Lower}+\\s?)){1,3}");

    @Override
    public void validate(User user) {
        validateStringParam(user, NAME_PATTERN, User::getFirstName, "First name is invalid");
        validateStringParam(user, NAME_PATTERN, User::getLastName, "Last name is invalid");
        validateStringParam(user, PASSWORD_PATTERN, User::getPassword, "Password is invalid");
        validateStringParam(user, EMAIL_PATTERN, User::getEmail, "Email address is invalid");
    }

    private void validateStringParam(User user, Pattern pattern, Function<User, String> function,
                                     String exceptionMessage) {
        Optional.ofNullable(user)
                .map(function)
                .filter(x -> pattern.matcher(x).matches())
                .orElseThrow(() -> new UserValidatorException(exceptionMessage));
    }
}
