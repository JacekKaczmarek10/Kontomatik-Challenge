package bank.pkobp.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bank.pkobp.entity.UserCredentials;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PropertiesLoaderTest {

    @Nested
    class LoadPropertiesTests {

        private static final String VALID_PROPERTIES = "login=testUser\n" + "password=testPassword";

        private static final String MISSING_LOGIN_PROPERTIES = "password=testPassword";

        private static final String MISSING_PASSWORD_PROPERTIES = "login=testUser";

        private ClassLoader mockClassLoader;

        @BeforeEach
        void setUp() {
            mockClassLoader = mock(ClassLoader.class);
        }

        @Test
        void shouldLoadValidProperties() {
            final var inputStream = new ByteArrayInputStream(VALID_PROPERTIES.getBytes());
            when(mockClassLoader.getResourceAsStream("valid.properties")).thenReturn(inputStream);

            final var userCredentials = PropertiesLoader.loadProperties("valid.properties", mockClassLoader);

            assertThat(userCredentials).isNotNull();
            assertThat(userCredentials.login()).isEqualTo("testUser");
            assertThat(userCredentials.password()).isEqualTo("testPassword");
        }

        @Test
        void shouldHandlePropertiesFileNotFound() {
            when(mockClassLoader.getResourceAsStream("nonexistent.properties")).thenReturn(null);

            final var userCredentials = PropertiesLoader.loadProperties("nonexistent.properties", mockClassLoader);

            assertThat(userCredentials).isNull();
        }

        @Test
        void shouldThrowExceptionOnMissingLogin() {
            final var inputStream = new ByteArrayInputStream(MISSING_LOGIN_PROPERTIES.getBytes());
            when(mockClassLoader.getResourceAsStream("missing_login.properties")).thenReturn(inputStream);

            assertThatThrownBy(() -> PropertiesLoader.loadProperties("missing_login.properties", mockClassLoader)).isInstanceOf(
                NullPointerException.class).hasMessage("Login is missing or empty.");
            verify(mockClassLoader).getResourceAsStream("missing_login.properties");
        }

        @Test
        void shouldThrowExceptionOnMissingPassword() {
            final var inputStream = new ByteArrayInputStream(MISSING_PASSWORD_PROPERTIES.getBytes());
            when(mockClassLoader.getResourceAsStream("missing_password.properties")).thenReturn(inputStream);

            assertThatThrownBy(() -> PropertiesLoader.loadProperties("missing_password.properties",
                                                                     mockClassLoader)).isInstanceOf(NullPointerException.class)
                .hasMessage("Password is missing or empty.");
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
        void shouldValidatePropertiesWhenBothPresent() {
            final var validCredentials = new UserCredentials("username", "password");

            assertThatCode(() -> PropertiesLoader.validateProperties(validCredentials)).doesNotThrowAnyException();
        }

        @Test
        void shouldThrowExceptionOnNullCredentials() {
            assertThatThrownBy(() -> PropertiesLoader.validateProperties(null)).isInstanceOf(NullPointerException.class);

            assertThat(systemOutContent.toString().trim()).isEmpty(); // Ensure no output to System.out
        }
    }
}