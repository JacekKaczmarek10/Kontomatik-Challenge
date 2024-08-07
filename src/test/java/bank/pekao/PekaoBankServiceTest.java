package bank.pekao;

import bank.pkobp.entity.UserCredentials;
import bank.pkobp.utils.PropertiesLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PekaoBankServiceTest {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @InjectMocks
    @Spy
    private PekaoBankService pekaoBankService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {

        try (var ignored = MockitoAnnotations.openMocks(this)) {
            System.setErr(new PrintStream(errContent));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
    }

    @Nested
    class LoginAndGetAccountDataTest {

        @BeforeEach
        void setUp() throws IOException {
            try (final var mockedStatic = mockStatic(PropertiesLoader.class)) {
                mockedStatic.when(() -> PropertiesLoader.loadProperties(any(String.class))).thenAnswer(invocation -> null);
                doReturn("test").when(pekaoBankService).requestPasswordMask();
                doReturn("test").when(pekaoBankService).parsePasswordMask(any());
                doReturn("test").when(pekaoBankService).extractMaskedPassword(any());
                doNothing().when(pekaoBankService).login(any());
            }
        }

        @Test
        void shouldLoadProperties() throws IOException {
            try (final var mockedStatic = Mockito.mockStatic(PropertiesLoader.class)) {
                callService();
                mockedStatic.verify(() -> PropertiesLoader.loadProperties(any(String.class)));
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

            verify(pekaoBankService).executePostRequest(any(String.class), any(String.class));

        }

        private void callService() throws IOException {
            pekaoBankService.requestPasswordMask();
        }
    }

    @Nested
    class ExtractMaskedPasswordTest {

        @Test
        void shouldReturnMaskedPassword() {
            pekaoBankService.setUserCredentials(new UserCredentials(null, "password"));

            final var result = callService();

            assertThat(result).isEqualTo("pswd");
        }

        private String callService() {
            return pekaoBankService.extractMaskedPassword("10101001");
        }
    }

    @Nested
    class ParsePasswordMaskTest {

        @Test
        void shouldReturnPasswordMask() throws IOException {
            final var result = callService();

            assertThat(result).isEqualTo("test");
        }

        private String callService() throws IOException {
            return pekaoBankService.parsePasswordMask("{\"passwordMask\":\"test\"}");
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
            return pekaoBankService.executePostRequest("https://www.pekao24.pl/api/authentication/customer/logon", "test");
        }

    }

    @Nested
    class CreatePasswordMaskRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            objectMapper = new ObjectMapper();
            pekaoBankService.setUserCredentials(new UserCredentials("test", null));

            final var passwordMask = callService();

            final var jsonObject = assertDoesNotThrow(() -> objectMapper.readTree(passwordMask));
            assertThat(jsonObject.get("customer").asText()).isEqualTo("test");
        }

        private String callService() {
            return pekaoBankService.createPasswordMaskRequestBody();
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
            objectMapper = new ObjectMapper();
            pekaoBankService.setUserCredentials(new UserCredentials("test", null));

            final var result = callService();

            assertDoesNotThrow(() -> {
                JsonNode jsonObject = objectMapper.readTree(result);
                assertThat(jsonObject.get("customer").asText()).isEqualTo("test");
                assertThat(jsonObject.get("password").asText()).isEqualTo("test");
            });
        }

        private String callService() {
            return pekaoBankService.createLoginRequestBody("test");
        }
    }

    @Nested
    class ConvertMapToJsonTest {

        @Test
        public void shouldConvertMapToJsonSuccess() throws Exception {
            Map<String, String> map = new HashMap<>();
            map.put("key1", "value1");
            map.put("key2", "value2");
            final var expectedJson = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
            when(objectMapper.writeValueAsString(map)).thenReturn(expectedJson);

            final var jsonResult = pekaoBankService.convertMapToJson(map);

            assertThat(jsonResult).isEqualTo(expectedJson);
        }

        @Test
        public void shouldConvertMapToJsonEmptyMap() throws Exception {
            Map<String, String> map = new HashMap<>();
            final var expectedJson = "{}";
            when(objectMapper.writeValueAsString(map)).thenReturn(expectedJson);

            final var jsonResult = pekaoBankService.convertMapToJson(map);

            assertThat(jsonResult).isEqualTo(expectedJson);
        }

    }
}