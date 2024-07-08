package bank.pkobp.utils;

import bank.pkobp.entity.UserCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertiesLoaderTest {

    @Nested
    class LoadPropertiesTests {

        private static final String VALID_PROPERTIES =
                "login=testUser\n" +
                        "password=testPassword";

        private static final String MISSING_LOGIN_PROPERTIES =
                "password=testPassword";

        private static final String MISSING_PASSWORD_PROPERTIES =
                "login=testUser";

        private ClassLoader mockClassLoader;

        @BeforeEach
        void setUp() {
            mockClassLoader = mock(ClassLoader.class);
        }

        @Test
        void testLoadValidProperties() {
            InputStream inputStream = new ByteArrayInputStream(VALID_PROPERTIES.getBytes());
            when(mockClassLoader.getResourceAsStream("valid.properties")).thenReturn(inputStream);

            UserCredentials userCredentials = PropertiesLoader.loadProperties("valid.properties", mockClassLoader);

            assertNotNull(userCredentials);
            assertEquals("testUser", userCredentials.login());
            assertEquals("testPassword", userCredentials.password());
        }

        @Test
        void testLoadPropertiesFileNotFound() {
            when(mockClassLoader.getResourceAsStream("nonexistent.properties")).thenReturn(null);

            UserCredentials userCredentials = PropertiesLoader.loadProperties("nonexistent.properties", mockClassLoader);

            assertNull(userCredentials);
        }

        @Test
        void testLoadPropertiesMissingLogin() {
            InputStream inputStream = new ByteArrayInputStream(MISSING_LOGIN_PROPERTIES.getBytes());
            when(mockClassLoader.getResourceAsStream("missing_login.properties")).thenReturn(inputStream);

            NullPointerException exception = assertThrows(NullPointerException.class, () ->
                    PropertiesLoader.loadProperties("missing_login.properties", mockClassLoader));

            assertEquals("Login is missing or empty.", exception.getMessage());

            verify(mockClassLoader).getResourceAsStream("missing_login.properties");
        }

        @Test
        void testLoadPropertiesMissingPassword() {
            InputStream inputStream = new ByteArrayInputStream(MISSING_PASSWORD_PROPERTIES.getBytes());
            when(mockClassLoader.getResourceAsStream("missing_password.properties")).thenReturn(inputStream);

            NullPointerException exception = assertThrows(NullPointerException.class, () ->
                    PropertiesLoader.loadProperties("missing_password.properties", mockClassLoader));

            assertEquals("Password is missing or empty.", exception.getMessage());

            verify(mockClassLoader).getResourceAsStream("missing_password.properties");
        }
    }

    @Nested
    class ValidatePropertiesTests {

        private final PrintStream originalSystemOut = System.out;
        private final ByteArrayOutputStream systemOutContent = new ByteArrayOutputStream();

        @BeforeEach
        void setUpStreams() {
            System.setOut(new PrintStream(systemOutContent));
        }

        @AfterEach
        void restoreStreams() {
            System.setOut(originalSystemOut);
        }

        @Test
        void testValidateProperties_BothPropertiesPresent() {
            UserCredentials validCredentials = new UserCredentials("username", "password");

            assertDoesNotThrow(() -> PropertiesLoader.validateProperties(validCredentials));
        }

        @Test
        void testValidateProperties_NullCredentials() {
            assertThrows(NullPointerException.class, () -> PropertiesLoader.validateProperties(null));

            assertEquals("", systemOutContent.toString().trim());
        }
    }
}