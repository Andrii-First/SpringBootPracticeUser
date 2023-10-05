package project.practice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource("classpath:application.properties")
public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {
    @Autowired
    private Environment environment;

    @Override
    public boolean isValid(LocalDate birthDate,
                           ConstraintValidatorContext constraintValidatorContext) {
        return birthDate.plusYears(Long.parseLong(Objects
                        .requireNonNull(environment.getProperty("minAge"))))
                .isBefore(LocalDate.now());
    }
}
