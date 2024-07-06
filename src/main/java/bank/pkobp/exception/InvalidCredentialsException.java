package bank.pkobp.exception;

public class InvalidCredentialsException extends RequestProcessingException{

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
