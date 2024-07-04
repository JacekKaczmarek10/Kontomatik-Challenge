package bank.pko.pkobp.exception;

public class InvalidCredentialsException extends RequestProcessingException{

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
