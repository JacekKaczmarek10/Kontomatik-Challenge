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
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class PerformLoginTest {

        @BeforeEach
        void setUp() throws IOException {
            doNothing().when(bankService).loadProperties();
            doReturn("test").when(bankService).requestPasswordMask();
            doReturn("test").when(bankService).parsePasswordMask(any());
            doReturn("test").when(bankService).extractMaskedPassword(any());
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
            doReturn("test").when(bankService).createPasswordMaskRequestBody();
            doReturn("test").when(bankService).executePostRequest(any(), any());
        }

        @Test
        void shouldCreatePasswordMaskRequestBody() throws IOException {
            callService();

            verify(bankService).createPasswordMaskRequestBody();
        }

        @Test
        void shouldExecutePostRequest() throws IOException {
            callService();

            verify(bankService).createPasswordMaskRequestBody();
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
            final var result = callService("{\"passwordMask\":\"test\"}");

            assertThat(result).isEqualTo("test");
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

            final var result = callService("10101001");

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
            doReturn("test").when(bankService).createLoginRequestBody(any());
            doReturn("test").when(bankService).executePostRequest(any(), any());
        }

        @Test
        void shouldCreateLoginRequestBodyTest() throws IOException {
            callService("test");

            verify(bankService).createLoginRequestBody("test");
        }

        @Test
        void shouldExecutePostRequestTest() throws IOException {
            callService("test");

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

            final var result = callService("test");

            final var jsonObject = gson.fromJson(result, JsonObject.class);
            assertThat(jsonObject.get("customer").getAsString()).isEqualTo("test");
            assertThat(jsonObject.get("password").getAsString()).isEqualTo("test");
        }

        private String callService(String maskedPassword) {
            return bankService.createLoginRequestBody(maskedPassword);
        }
    }
}