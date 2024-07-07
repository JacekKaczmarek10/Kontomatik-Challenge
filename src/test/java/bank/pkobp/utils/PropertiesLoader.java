package bank.pkobp.utils;

import bank.pkobp.entity.UserCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertiesLoaderTest {

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

        UserCredentials userCredentials = PropertiesLoader.loadProperties("missing_login.properties", mockClassLoader);

        assertNull(userCredentials);
    }

    @Test
    void testLoadPropertiesMissingPassword() {
        InputStream inputStream = new ByteArrayInputStream(MISSING_PASSWORD_PROPERTIES.getBytes());
        when(mockClassLoader.getResourceAsStream("missing_password.properties")).thenReturn(inputStream);

        UserCredentials userCredentials = PropertiesLoader.loadProperties("missing_password.properties", mockClassLoader);

        assertNull(userCredentials);
    }
}