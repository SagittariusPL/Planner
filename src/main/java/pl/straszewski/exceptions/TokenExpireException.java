package pl.straszewski.exceptions;

public class TokenExpireException extends RuntimeException {
    public TokenExpireException(String message) {
        super(message);
    }
}
