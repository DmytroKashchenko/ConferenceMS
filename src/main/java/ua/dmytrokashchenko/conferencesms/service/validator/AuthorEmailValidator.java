package ua.dmytrokashchenko.conferencesms.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.dmytrokashchenko.conferencesms.domain.Role;
import ua.dmytrokashchenko.conferencesms.domain.User;
import ua.dmytrokashchenko.conferencesms.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class AuthorEmailValidator implements ConstraintValidator<AuthorEmail, String> {
    private final UserService userService;

    @Override
    public void initialize(AuthorEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean registered = userService.isRegistered(email);
        if (registered) {
            User user = userService.getByEmail(email);
            return user.getRole().equals(Role.SPEAKER);
        }
        return false;
    }
}
