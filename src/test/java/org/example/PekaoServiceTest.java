package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PekaoServiceTest {

    @InjectMocks
    @Spy
    private PekaoService pekaoService;
    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class PerformLoginTest {

        @BeforeEach
        void setUp() throws IOException {
            doNothing().when(pekaoService).loadProperties();
            doReturn("test").when(pekaoService).requestPasswordMask();
            doReturn("test").when(pekaoService).parsePasswordMask(any());
            doReturn("test").when(pekaoService).extractMaskedPassword(any());
            doNothing().when(pekaoService).login(any());
        }

        @Test
        void shouldLoadProperties() throws IOException {

            callService();

            verify(pekaoService).loadProperties();
        }

        @Test
        void shouldRequestPasswordMask() throws IOException {

            callService();

            verify(pekaoService).requestPasswordMask();
        }

        @Test
        void shouldParsePasswordMask() throws IOException {

            callService();

            verify(pekaoService).parsePasswordMask(any());
        }

        @Test
        void shouldExtractMaskedPassword() throws IOException {

            callService();

            verify(pekaoService).extractMaskedPassword(any());
        }

        @Test
        void shouldLogin() throws IOException {

            callService();

            verify(pekaoService).login(any());
        }

        private void callService() throws IOException {
            pekaoService.performLogin();
        }
    }

    @Nested
    class RequestPasswordMaskTest {

        @BeforeEach
        void setUp() throws IOException {
            doReturn("test").when(pekaoService).createPasswordMaskRequestBody();
            doReturn("test").when(pekaoService).executePostRequest(any(), any());
        }

        @Test
        void shouldCreatePasswordMaskRequestBody() throws IOException {

            callService();

            verify(pekaoService).createPasswordMaskRequestBody();
        }

        @Test
        void shouldExecutePostRequest() throws IOException {

            callService();

            verify(pekaoService).createPasswordMaskRequestBody();
        }

        private void callService() throws IOException {
            pekaoService.requestPasswordMask();
        }
    }

    @Nested
    class CreatePasswordMaskRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            pekaoService.username = "test";

            String result = callService();

            JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
            assertEquals("test", jsonObject.get("customer").getAsString());
        }

        private String callService() {
            return pekaoService.createPasswordMaskRequestBody();
        }
    }

    @Nested
    class ParsePasswordMaskTest {

        @Test
        void shouldReturnPasswordMask() {

            String result = callService("{\"passwordMask\":\"test\"}");

            assertEquals("test", result);
        }

        private String callService(String response) {
            return pekaoService.parsePasswordMask(response);
        }
    }

    @Nested
    class ExtractMaskedPasswordTest {

        @Test
        void shouldReturnMaskedPassword() {
            pekaoService.password = "password";

            String result = callService("10101001");

            assertEquals("pswd", result);
        }

        private String callService(String passwordMask) {
            return pekaoService.extractMaskedPassword(passwordMask);
        }
    }

    @Nested
    class LoginTest {

        @BeforeEach
        void setUp() throws IOException {
            doReturn("test").when(pekaoService).createLoginRequestBody(any());
            doReturn("test").when(pekaoService).executePostRequest(any(), any());
        }

        @Test
        void shouldCreateLoginRequestBodyTest() throws IOException {

            callService("test");

            verify(pekaoService).createLoginRequestBody("test");
        }

        @Test
        void shouldExecutePostRequestTest() throws IOException {

            callService("test");

            verify(pekaoService).executePostRequest(any(), any());
        }

        private void callService(String passwordMask) throws IOException {
             pekaoService.login(passwordMask);
        }
    }

    @Nested
    class CreateLoginRequestBodyTest {

        @Test
        void shouldReturnJsonString() {
            pekaoService.username = "test";

            String result = callService("test");

            JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
            assertEquals("test", jsonObject.get("customer").getAsString());
            assertEquals("test", jsonObject.get("password").getAsString());
        }

        private String callService(String maskedPassword) {
            return pekaoService.createLoginRequestBody(maskedPassword);
        }
    }
}