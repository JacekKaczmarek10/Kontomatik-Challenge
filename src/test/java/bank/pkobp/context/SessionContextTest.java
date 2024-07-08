package bank.pkobp.context;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SessionContextTest {

    @Nested
    class GetInstanceTest {

        @Test
        public void shouldGetInstance() {
            final var instance1 = SessionContext.getInstance();
            final var instance2 = SessionContext.getInstance();

            assertNotNull(instance1, "Instance1 should not be null");
            assertNotNull(instance2, "Instance2 should not be null");
            assertSame(instance1, instance2, "Both instances should be the same");
        }

    }

    @Nested
    class GetSessionIdTest {

        @Test
        public void shouldSetAndGetSessionId() {
            final var instance = SessionContext.getInstance();
            instance.setSessionId("12345");

            assertEquals("12345", instance.getSessionId(), "Session ID should be '12345'");
        }

    }


    @Nested
    class SetAndGetSessionIdTest {

        @Test
        public void shouldSetSessionId() {
            final var instance = SessionContext.getInstance();
            instance.setSessionId("abcdef");

            assertEquals("abcdef", instance.getSessionId(), "Session ID should be 'abcdef'");
        }

    }


}
