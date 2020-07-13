package pl.straszewski.exceptions;

public class MeetingIsActualException extends RuntimeException {
    public MeetingIsActualException(String message) {
        super(message);
    }
}
