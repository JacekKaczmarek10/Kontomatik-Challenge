package bank;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.lang.AutoCloseable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BankServiceTest {

    @InjectMocks
    @Spy
    private BankService bankService;

    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            // No additional setup required here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nested
    class PerformLoginTest {

        @BeforeEach
        void setUp() throws IOException {
            doNothing().when(bankService).loadProperties();
            doReturn("test").when(bankService).requestPasswordMask();
            doReturn("parsedTest").when(bankService).parsePasswordMask(any());
            doReturn("maskedTest").when(bankService).extractMaskedPassword(any());
            doNothing().when(bankService).login(any());
        }

        @Test
        void shouldLoadProperties() throws IOException {
            callService();

            verify(bankService).loadProperties();
        }

        @Test
        void shouldRequestPasswordMask() throws IOException {
            callService();

            verify(bankService).requestPasswordMask();
        }

        @Test
        void shouldParsePasswordMask() throws IOException {
            callService();

            verify(bankService).parsePasswordMask(any());
        }

        @Test
        void shouldExtractMaskedPassword() throws IOException {
            callService();

            verify(bankService).extractMaskedPassword(any());
        }

        @Test
        void shouldLogin() throws IOException {
            callService();

            verify(bankService).login(any());
        }

        private void callService() throws IOException {
            bankService.performLogin();
        }
    }

    @Nested
    class RequestPasswordMaskTest {

        @BeforeEach
        void setUp() throws IOException {
            doReturn("requestBodyTest").when(bankService).createPasswordMaskRequestBody();
            doReturn("responseTest").when(bankService).executePostRequest(any(), any());
        }

        @Test
        void shouldCreatePasswordMaskRequestBody() throws IOException {
            callService();

            verify(bankService).createPasswordMaskRequestBody();
        }

        @Test
        void shouldExecutePostRequest() throws IOException {
            callService();

            verify(bankService).executePostRequest(any(), any());
        }

        private void callService() throws IOException {
            bankService.requestPasswordMask();
        }
    }

    @Nested
    class CreatePasswordMaskRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            bankService.username = "test";

            final var passwordMask = callService();

            final var jsonObject = gson.fromJson(passwordMask, JsonObject.class);
            assertThat(jsonObject.get("customer").getAsString()).isEqualTo("test");
        }

        private String callService() {
            return bankService.createPasswordMaskRequestBody();
        }
    }

    @Nested
    class ParsePasswordMaskTest {

        @Test
        void shouldReturnPasswordMask() {
            final var result = callService("{\"passwordMask\":\"parsedTest\"}");

            assertThat(result).isEqualTo("parsedTest");
        }

        private String callService(String response) {
            return bankService.parsePasswordMask(response);
        }
    }

    @Nested
    class ExtractMaskedPasswordTest {

        @Test
        void shouldReturnMaskedPassword() {
            bankService.password = "password";

            final var result = callService("01001010");

            assertEquals("pswd", result);
        }

        private String callService(String passwordMask) {
            return bankService.extractMaskedPassword(passwordMask);
        }
    }

    @Nested
    class LoginTest {

        @BeforeEach
        void setUp() throws IOException {
            doReturn("loginRequestBodyTest").when(bankService).createLoginRequestBody(any());
            doReturn("loginResponseTest").when(bankService).executePostRequest(any(), any());
        }

        @Test
        void shouldCreateLoginRequestBody() throws IOException {
            callService("parsedTest");

            verify(bankService).createLoginRequestBody("parsedTest");
        }

        @Test
        void shouldExecutePostRequest() throws IOException {
            callService("parsedTest");

            verify(bankService).executePostRequest(any(), any());
        }

        private void callService(String passwordMask) throws IOException {
            bankService.login(passwordMask);
        }
    }

    @Nested
    class CreateLoginRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            bankService.username = "test";

            final var result = callService("maskedTest");

            final var jsonObject = gson.fromJson(result, JsonObject.class);
            assertThat(jsonObject.get("customer").getAsString()).isEqualTo("test");
            assertThat(jsonObject.get("password").getAsString()).isEqualTo("maskedTest");
        }

        private String callService(String maskedPassword) {
            return bankService.createLoginRequestBody(maskedPassword);
        }
    }
}
