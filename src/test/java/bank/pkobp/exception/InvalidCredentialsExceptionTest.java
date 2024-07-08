package bank.pkobp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidCredentialsExceptionTest {

    @Test
    void shouldThrowInvalidCredentialsException() {
        final var exception = assertThrows(InvalidCredentialsException.class, () -> {
            throw new InvalidCredentialsException("Invalid credentials");
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }
}
