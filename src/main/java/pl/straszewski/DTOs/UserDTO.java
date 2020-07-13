package pl.straszewski.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.straszewski.exceptions.IncorrectDataException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
@Setter
public class UserDTO {

    @NotBlank(message = "Insert name")
    private String name;

    @NotBlank(message = "Insert password")
    private String password;

    private String role ="ROLE_USER";

    @NotBlank(message = "Insert email")
    @Email(message = "Incorrect email")
    private String email;


    public boolean isValid() {

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserDTO>> validationErrors = validator.validate(this);

        if (validationErrors.size() == 0) {
            return true;
        }
        throw new IncorrectDataException(validationErrors.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList())
        );
    }
}
