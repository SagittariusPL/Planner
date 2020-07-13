package pl.straszewski.exceptions;

public class MeetingBeyond8And18Exception extends RuntimeException {
    public MeetingBeyond8And18Exception(String message) {
        super(message);
    }
}
