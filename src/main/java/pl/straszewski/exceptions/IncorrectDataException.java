package pl.straszewski.exceptions;

import lombok.Getter;

import java.util.List;
@Getter
public class IncorrectDataException extends RuntimeException {

    private List<String> errors;

    public IncorrectDataException(List<String> errors)
    {
        super();
        this.errors=errors;
    }
}
