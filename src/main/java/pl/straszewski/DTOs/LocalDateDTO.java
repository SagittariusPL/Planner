package pl.straszewski.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.straszewski.exceptions.IncorrectDataException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class LocalDateDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please choose a date")
    private LocalDate localDate;


    public boolean isValid() {

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<LocalDateDTO>> validationErrors = validator.validate(this);

        if (validationErrors.size() == 0) {
            return true;
        }
        throw new IncorrectDataException(validationErrors.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList())
        );
    }
}
