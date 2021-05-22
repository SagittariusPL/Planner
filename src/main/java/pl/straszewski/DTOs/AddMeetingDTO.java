package pl.straszewski.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.straszewski.exceptions.IncorrectDataException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AddMeetingDTO {


    @NotBlank(message = "Title can not be empty")
    String title;

    //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Zagreb")
    @NotNull(message = "Please define meeting start date")
    @FutureOrPresent(message = "Can not to plan meeting in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime meetingStartDate;


    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Zagreb")
    @NotNull(message = "Please define meeting end date")
    @FutureOrPresent(message = "Can not to plan meeting in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime meetingEndDate;


    @NotBlank(message = "Please define number of room")
    @Min(value = 1, message = "Value of room number must be between  1-5 ")
    @Max(value = 5, message = "Value of room number must be between  1-5 ")
    String room;

    String description;


    public boolean isValid() {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<AddMeetingDTO>> validationErrors = validator.validate(this);

        if (validationErrors.size() == 0) {
            return true;
        }
        //add something
        throw new IncorrectDataException(validationErrors.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList())
        );
    }
}
