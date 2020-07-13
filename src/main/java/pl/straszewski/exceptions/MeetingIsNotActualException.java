package pl.straszewski.exceptions;

public class MeetingIsNotActualException extends RuntimeException {
    public MeetingIsNotActualException(String message) {
        super(message);
    }
}
