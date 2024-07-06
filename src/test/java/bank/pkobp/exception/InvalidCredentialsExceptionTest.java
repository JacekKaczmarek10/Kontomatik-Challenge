package bank.pkobp.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class InvalidCredentialsExceptionTest {

    @Test
    void testInvalidCredentialsException() {

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            throw new InvalidCredentialsException("Invalid credentials");
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }
}
