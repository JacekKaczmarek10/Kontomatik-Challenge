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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

    @Test
    void testExecutePipeline() throws IOException, RequestProcessingException {
        AuthResponse mockLoginResponse = new AuthResponse("mockLoginToken", "flowId");
        AuthResponse mockPasswordResponse = new AuthResponse("mockPasswordToken", "flowId");
        List<Account> mockAccounts = Collections.singletonList(new Account("Savings", "1000.0"));
        String mockJson = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";

        when(mockLoginProcessor.postRequest(any(), any())).thenReturn(mockLoginResponse);
        when(mockPasswordProcessor.postRequest(any(), any())).thenReturn(mockPasswordResponse);
        when(mockOtpProcessor.postRequest(any(), any())).thenReturn(mockLoginResponse);
        when(mockAccountsProcessor.postRequest(eq(mockJson), any())).thenReturn(mockAccounts);
        when(mockReader.readLine()).thenReturn("123456");

        List<Account> result = signPipeline.executePipeline();

        assertEquals(mockAccounts, result);
        verify(mockLoginProcessor, times(1)).postRequest(any(), any());
        verify(mockPasswordProcessor, times(1)).postRequest(any(), any());
        verify(mockOtpProcessor, times(1)).postRequest(any(), any());
        verify(mockAccountsProcessor, times(1)).postRequest(any(), any());
        verify(mockReader, times(1)).readLine();
    }

    @Test
    void testReadOTPFromUser() throws IOException {
        when(mockReader.readLine()).thenReturn("123456\n");

        String otp = signPipeline.readOTPFromUser();
        assertEquals("123456", otp);
    }

    @Test
    void testLoginRequest() throws IOException, RequestProcessingException {
        AuthResponse mockResponse = new AuthResponse("mockToken", "flowId");
        LoginSubmitRequest mockRequest = new LoginSubmitRequest("username");

        when(mockLoginProcessor.postRequest(eq(mockRequest), any())).thenReturn(mockResponse);

        AuthResponse result = signPipeline.loginRequest();

        assertEquals(mockResponse, result);
        verify(mockLoginProcessor, times(1)).postRequest(eq(mockRequest), any());
    }

    @Test
    void testPasswordRequest() throws IOException, RequestProcessingException {
        AuthResponse mockResponse = new AuthResponse("token", "flowId");
        AuthResponse mockLoginResponse = new AuthResponse("token", "flowId");
        PasswordSubmitRequest mockRequest = new PasswordSubmitRequest(mockLoginResponse, "password");

        when(mockPasswordProcessor.postRequest(eq(mockRequest), any())).thenReturn(mockResponse);

        AuthResponse result = signPipeline.passwordRequest(mockLoginResponse);

        assertEquals(mockResponse, result);
        verify(mockPasswordProcessor, times(1)).postRequest(eq(mockRequest), any());
    }

    @Test
    void testOtpRequest() throws IOException, RequestProcessingException {
        AuthResponse mockResponse = new AuthResponse("token", "flowId");
        OTPSubmitRequest mockRequest = new OTPSubmitRequest(mockResponse, "123456");

        when(mockOtpProcessor.postRequest(eq(mockRequest), any())).thenReturn(mockResponse);

        signPipeline.otpRequest("123456", mockResponse);

        verify(mockOtpProcessor, times(1)).postRequest(eq(mockRequest), any());
    }

    @Test
    void testAccountDataRequest() throws IOException, RequestProcessingException {
        List<Account> mockAccounts = Collections.singletonList(new Account("Savings", "1000.0"));
        String mockJson = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";

        when(mockAccountsProcessor.postRequest(eq(mockJson), any())).thenReturn(mockAccounts);

        List<Account> result = signPipeline.accountDataRequest();

        assertEquals(mockAccounts, result);
        verify(mockAccountsProcessor, times(1)).postRequest(eq(mockJson), any());
    }

}