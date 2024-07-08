package bank.pkobp.request_pipelines;

import bank.pkobp.entity.Account;
import bank.pkobp.entity.UserCredentials;
import bank.pkobp.entity.request.LoginSubmitRequest;
import bank.pkobp.entity.request.OTPSubmitRequest;
import bank.pkobp.entity.request.PasswordSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.request_processors.GetAccountsInfoRequestProcessor;
import bank.pkobp.request_processors.LoginSubmitRequestProcessor;
import bank.pkobp.request_processors.OTPSubmitRequestProcessor;
import bank.pkobp.request_processors.PasswordSubmitRequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PKOBPSignPipelineTest {

    @Mock
    private LoginSubmitRequestProcessor mockLoginProcessor;

    @Mock
    private PasswordSubmitRequestProcessor mockPasswordProcessor;

    @Mock
    private OTPSubmitRequestProcessor mockOtpProcessor;

    @Mock
    private GetAccountsInfoRequestProcessor mockAccountsProcessor;

    @Mock
    private BufferedReader mockReader;

    @InjectMocks
    private PKOBPSignPipeline signPipeline;

    @BeforeEach
    public void setUp() {
        signPipeline = new PKOBPSignPipeline(new UserCredentials("username", "password"));
        signPipeline.setOtpReader(mockReader);
        signPipeline.setOtpSubmitRequestProcessor(mockOtpProcessor);
        signPipeline.setLoginSubmitRequestProcessor(mockLoginProcessor);
        signPipeline.setPasswordSubmitRequestProcessor(mockPasswordProcessor);
        signPipeline.setOtpSubmitRequestProcessor(mockOtpProcessor);
        signPipeline.setGetAccountsInfoRequestProcessor(mockAccountsProcessor);
    }

    @Nested
    class ExecutePipelineTest {

        @Test
        void shouldExecutePipeline() throws IOException, RequestProcessingException {
            final var mockLoginResponse = new AuthResponse("mockLoginToken", "flowId");
            final var mockPasswordResponse = new AuthResponse("mockPasswordToken", "flowId");
            final var mockAccounts = Collections.singletonList(new Account("Savings", "1000.0"));
            final var mockJson = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";
            when(mockLoginProcessor.postRequest(any(), any())).thenReturn(mockLoginResponse);
            when(mockPasswordProcessor.postRequest(any(), any())).thenReturn(mockPasswordResponse);
            when(mockOtpProcessor.postRequest(any(), any())).thenReturn(mockLoginResponse);
            when(mockAccountsProcessor.postRequest(eq(mockJson), any())).thenReturn(mockAccounts);
            when(mockReader.readLine()).thenReturn("123456");

            final var result = signPipeline.executePipeline();

            assertEquals(mockAccounts, result);
            verify(mockLoginProcessor, times(1)).postRequest(any(), any());
            verify(mockPasswordProcessor, times(1)).postRequest(any(), any());
            verify(mockOtpProcessor, times(1)).postRequest(any(), any());
            verify(mockAccountsProcessor, times(1)).postRequest(any(), any());
            verify(mockReader, times(1)).readLine();
        }

    }

    @Nested
    class ReadOTPFromUserTest {

        @Test
        void shouldReadOTPFromUser() throws IOException {
            when(mockReader.readLine()).thenReturn("123456\n");

            final var otp = signPipeline.readOTPFromUser();

            assertEquals("123456", otp);
        }

    }

    @Nested
    class LoginRequestTest {

        @Test
        void shouldLoginRequest() throws IOException, RequestProcessingException {
            final var mockResponse = new AuthResponse("mockToken", "flowId");
            final var mockRequest = new LoginSubmitRequest("username");
            when(mockLoginProcessor.postRequest(eq(mockRequest), any())).thenReturn(mockResponse);

            final var result = signPipeline.loginRequest();

            assertEquals(mockResponse, result);
            verify(mockLoginProcessor, times(1)).postRequest(eq(mockRequest), any());
        }
    }

    @Nested
    class PasswordRequestTest {

        @Test
        void shouldPasswordRequest() throws IOException, RequestProcessingException {
            final var mockResponse = new AuthResponse("token", "flowId");
            final var mockLoginResponse = new AuthResponse("token", "flowId");
            final var mockRequest = new PasswordSubmitRequest(mockLoginResponse, "password");
            when(mockPasswordProcessor.postRequest(eq(mockRequest), any())).thenReturn(mockResponse);

            final var result = signPipeline.passwordRequest(mockLoginResponse);

            assertEquals(mockResponse, result);
            verify(mockPasswordProcessor, times(1)).postRequest(eq(mockRequest), any());
        }

    }

    @Nested
    class OtpRequestTest {

        @Test
        void shouldOtpRequest() throws IOException, RequestProcessingException {
            final var mockResponse = new AuthResponse("token", "flowId");
            final var mockRequest = new OTPSubmitRequest(mockResponse, "123456");
            when(mockOtpProcessor.postRequest(eq(mockRequest), any())).thenReturn(mockResponse);

            signPipeline.otpRequest("123456", mockResponse);

            verify(mockOtpProcessor, times(1)).postRequest(eq(mockRequest), any());
        }

    }

    @Nested
    class AccountDataRequestTest {

        @Test
        void shouldAccountDataRequest() throws IOException, RequestProcessingException {
            final var mockAccounts = Collections.singletonList(new Account("Savings", "1000.0"));
            final var mockJson = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";
            when(mockAccountsProcessor.postRequest(eq(mockJson), any())).thenReturn(mockAccounts);

            final var result = signPipeline.accountDataRequest();

            assertEquals(mockAccounts, result);
            verify(mockAccountsProcessor, times(1)).postRequest(eq(mockJson), any());
        }

    }

}