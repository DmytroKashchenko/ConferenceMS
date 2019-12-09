package ua.dmytrokashchenko.conferencesms.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = AuthorEmailValidator.class)
public @interface AuthorEmail {
    String message() default "This is not an author email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
