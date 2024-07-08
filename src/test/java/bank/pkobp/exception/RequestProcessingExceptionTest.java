package bank.pkobp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestProcessingExceptionTest {

    @Test
    void shouldThrowRequestProcessingException() {

        final var exception = assertThrows(RequestProcessingException.class, () -> {
            throw new RequestProcessingException("Request processing failed");
        });

        assertEquals("Request processing failed", exception.getMessage());
    }
}
