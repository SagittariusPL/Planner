package pl.straszewski.exceptions;

public class MeetingDurationException extends RuntimeException {
    public MeetingDurationException(String message) {
        super(message);
    }
}
