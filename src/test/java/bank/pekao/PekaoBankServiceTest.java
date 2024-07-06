package bank.pekao;

import bank.pkobp.entity.UserCredentials;
import bank.pkobp.utils.PropertiesLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PekaoBankServiceTest {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;
    private final Gson gson = new Gson();

    @InjectMocks
    @Spy
    private PekaoBankService pekaoBankService;

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
            try(MockedStatic<PropertiesLoader> mockedStatic = Mockito.mockStatic(PropertiesLoader.class)) {
                mockedStatic.when(PropertiesLoader::loadProperties).thenAnswer(invocation -> null);
                doReturn("test").when(pekaoBankService).requestPasswordMask();
                doReturn("test").when(pekaoBankService).parsePasswordMask(any());
                doReturn("test").when(pekaoBankService).extractMaskedPassword(any());
                doNothing().when(pekaoBankService).login(any());
            }
        }

        @Test
        void shouldLoadProperties() throws IOException {
            try(MockedStatic<PropertiesLoader> mockedStatic = Mockito.mockStatic(PropertiesLoader.class)) {
                callService();
                mockedStatic.verify(PropertiesLoader::loadProperties);
            }
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
            pekaoBankService.loginAndGetAccountData();
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
    class ExecutePostRequestTest {

        @Test
        void shouldReturnResponse() throws IOException {
            final var response = callService();

            assertThat(response).isNotNull();
        }

        private String callService() throws IOException {
            return pekaoBankService.executePostRequest("https://www.pekao24.pl/api/authentication/customer/logon","test");
        }

    }

    @Nested
    class CreatePasswordMaskRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            pekaoBankService.setUserCredentials(new UserCredentials("test", null));

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
            final var result = callService();

            assertThat(result).isEqualTo("test");
        }

        private String callService() {
            return pekaoBankService.parsePasswordMask("{\"passwordMask\":\"test\"}");
        }
    }

    @Nested
    class ExtractMaskedPasswordTest {

        @Test
        void shouldReturnMaskedPassword() {
            pekaoBankService.setUserCredentials(new UserCredentials(null, "password"));

            final var result = callService();

            assertEquals("pswd", result);
        }

        private String callService() {
            return pekaoBankService.extractMaskedPassword("10101001");
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
            callService();

            verify(pekaoBankService).createLoginRequestBody("test");
        }

        @Test
        void shouldExecutePostRequestTest() throws IOException {
            callService();

            verify(pekaoBankService).executePostRequest(any(), any());
        }

        private void callService() throws IOException {
             pekaoBankService.login("test");
        }
    }

    @Nested
    class CreateLoginRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            pekaoBankService.setUserCredentials(new UserCredentials("test", null));

            final var result = callService();

            final var jsonObject = gson.fromJson(result, JsonObject.class);
            assertThat(jsonObject.get("customer").getAsString()).isEqualTo("test");
            assertThat(jsonObject.get("password").getAsString()).isEqualTo("test");
        }

        private String callService() {
            return pekaoBankService.createLoginRequestBody("test");
        }
    }
}