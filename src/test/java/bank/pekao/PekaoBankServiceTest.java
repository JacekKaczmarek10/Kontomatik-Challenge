package bank.pekao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PekaoBankServiceTest {

    @InjectMocks
    @Spy
    private PekaoBankService pekaoBankService;

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
    }

    @Nested
    class PerformLoginTest {

        @BeforeEach
        void setUp() throws IOException {
            doNothing().when(pekaoBankService).loadProperties();
            doReturn("test").when(pekaoBankService).requestPasswordMask();
            doReturn("test").when(pekaoBankService).parsePasswordMask(any());
            doReturn("test").when(pekaoBankService).extractMaskedPassword(any());
            doNothing().when(pekaoBankService).login(any());
        }

        @Test
        void shouldLoadProperties() throws IOException {
            callService();

            verify(pekaoBankService).loadProperties();
        }

        @Test
        void shouldRequestPasswordMask() throws IOException {
            callService();

            verify(pekaoBankService).requestPasswordMask();
        }

        @Test
        void shouldParsePasswordMask() throws IOException {
            callService();

            verify(pekaoBankService).parsePasswordMask(any());
        }

        @Test
        void shouldExtractMaskedPassword() throws IOException {
            callService();

            verify(pekaoBankService).extractMaskedPassword(any());
        }

        @Test
        void shouldLogin() throws IOException {
            callService();

            verify(pekaoBankService).login(any());
        }

        private void callService() throws IOException {
            pekaoBankService.performLogin();
        }
    }

    @Nested
    class LoadPropertiesTests {

        @Test
        void shouldLoadPropertiesSuccessfully() {
            pekaoBankService.loadProperties();

            assertThat(pekaoBankService.username).isEqualTo("usernamePlaceholder");
            assertThat(pekaoBankService.password).isEqualTo("passwordPlaceholder");
        }

        @Test
        void shouldCallValidatePropertiesTest() {
            pekaoBankService.loadProperties();

            verify(pekaoBankService).validateProperties();
        }
    }

    @Nested
    class ValidatePropertiesTest {

        @Test
        void shouldPrintErrorWhenUsernameIsMissing() {
            pekaoBankService.username = null;
            pekaoBankService.password = "testPassword";

            pekaoBankService.validateProperties();

            assertThat(errContent.toString()).contains("Username is missing in the properties file.");
        }

        @Test
        void shouldPrintErrorWhenPasswordIsMissing() {
            pekaoBankService.username = "testUsername";
            pekaoBankService.password = null;

            pekaoBankService.validateProperties();

            assertThat(errContent.toString()).contains("Password is missing in the properties file.");
        }

        @Test
        void shouldPrintBothErrorsWhenUsernameAndPasswordAreMissing() {
            pekaoBankService.username = null;
            pekaoBankService.password = null;

            pekaoBankService.validateProperties();

            assertThat(errContent.toString()).contains("Username is missing in the properties file.")
                .contains("Password is missing in the properties file.");
        }

        @Test
        void shouldNotPrintErrorWhenUsernameAndPasswordArePresent() {
            pekaoBankService.username = "testUsername";
            pekaoBankService.password = "testPassword";

            pekaoBankService.validateProperties();

            assertThat(errContent.toString()).isEmpty();
        }

        @Test
        void shouldPrintErrorWhenUsernameIsEmpty() {
            pekaoBankService.username = "";
            pekaoBankService.password = "testPassword";

            pekaoBankService.validateProperties();

            assertThat(errContent.toString()).contains("Username is missing in the properties file.");
        }

        @Test
        void shouldPrintErrorWhenPasswordIsEmpty() {
            pekaoBankService.username = "testUsername";
            pekaoBankService.password = "";

            pekaoBankService.validateProperties();

            assertThat(errContent.toString()).contains("Password is missing in the properties file.");
        }

    }

    @Nested
    class RequestPasswordMaskTest {

        @BeforeEach
        void setUp() throws IOException {
            doReturn("test").when(pekaoBankService).createPasswordMaskRequestBody();
            doReturn("test").when(pekaoBankService).executePostRequest(any(), any());
        }

        @Test
        void shouldCreatePasswordMaskRequestBody() throws IOException {
            callService();

            verify(pekaoBankService).createPasswordMaskRequestBody();
        }

        @Test
        void shouldExecutePostRequest() throws IOException {
            callService();

            verify(pekaoBankService).createPasswordMaskRequestBody();
        }

        private void callService() throws IOException {
            pekaoBankService.requestPasswordMask();
        }
    }

    @Nested
    class CreatePasswordMaskRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            pekaoBankService.username = "test";

            final var passwordMask = callService();

            final var jsonObject = gson.fromJson(passwordMask, JsonObject.class);
            assertThat(jsonObject.get("customer").getAsString()).isEqualTo("test");
        }

        private String callService() {
            return pekaoBankService.createPasswordMaskRequestBody();
        }
    }

    @Nested
    class ParsePasswordMaskTest {

        @Test
        void shouldReturnPasswordMask() {
            final var result = callService("{\"passwordMask\":\"test\"}");

            assertThat(result).isEqualTo("test");
        }

        private String callService(String response) {
            return pekaoBankService.parsePasswordMask(response);
        }
    }

    @Nested
    class ExtractMaskedPasswordTest {

        @Test
        void shouldReturnMaskedPassword() {
            pekaoBankService.password = "password";

            final var result = callService("10101001");

            assertEquals("pswd", result);
        }

        private String callService(String passwordMask) {
            return pekaoBankService.extractMaskedPassword(passwordMask);
        }
    }

    @Nested
    class LoginTest {

        @BeforeEach
        void setUp() throws IOException {
            doReturn("test").when(pekaoBankService).createLoginRequestBody(any());
            doReturn("test").when(pekaoBankService).executePostRequest(any(), any());
        }

        @Test
        void shouldCreateLoginRequestBodyTest() throws IOException {
            callService("test");

            verify(pekaoBankService).createLoginRequestBody("test");
        }

        @Test
        void shouldExecutePostRequestTest() throws IOException {
            callService("test");

            verify(pekaoBankService).executePostRequest(any(), any());
        }

        private void callService(String passwordMask) throws IOException {
             pekaoBankService.login(passwordMask);
        }
    }

    @Nested
    class CreateLoginRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            pekaoBankService.username = "test";

            final var result = callService("test");

            final var jsonObject = gson.fromJson(result, JsonObject.class);
            assertThat(jsonObject.get("customer").getAsString()).isEqualTo("test");
            assertThat(jsonObject.get("password").getAsString()).isEqualTo("test");
        }

        private String callService(String maskedPassword) {
            return pekaoBankService.createLoginRequestBody(maskedPassword);
        }
    }

}