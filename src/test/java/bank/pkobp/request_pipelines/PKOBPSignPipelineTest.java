package bank.pkobp.request_pipelines;

import bank.pkobp.entity.Account;
import bank.pkobp.entity.UserCredentials;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class PKOBPSignPipelineTest {

    @Mock
    private LoginSubmitRequestProcessor loginSubmitRequestProcessor;

    @Mock
    private PasswordSubmitRequestProcessor passwordSubmitRequestProcessor;

    @Mock
    private OTPSubmitRequestProcessor otpSubmitRequestProcessor;

    @Mock
    private GetAccountsInfoRequestProcessor getAccountsInfoRequestProcessor;

    @InjectMocks
    private PKOBPSignPipeline signPipeline;

    @BeforeEach
    void setUp() {
        UserCredentials userCredentials = new UserCredentials("username", "password");
        signPipeline = Mockito.spy(new PKOBPSignPipeline(userCredentials));
    }

    @Test
    void testExecutePipeline_SuccessfulExecution() throws IOException, RequestProcessingException {
        // Mock loginRequest
        AuthResponse loginResponse = new AuthResponse("token", "flowId");
        Mockito.doReturn(loginResponse).when(signPipeline).loginRequest();

        // Mock passwordRequest
        AuthResponse passwordResponse = new AuthResponse("token", "flowId");
        Mockito.doReturn(passwordResponse).when(signPipeline).passwordRequest(any(AuthResponse.class));

        // Mock otpRequest
        doNothing().when(signPipeline).otpRequest(any(String.class), any(AuthResponse.class));

        // Mock accountDataRequest
        List<Account> accounts = Arrays.asList(
                new Account("12345", "Checking"),
                new Account("67890", "Savings")
        );
        Mockito.doReturn(accounts).when(signPipeline).accountDataRequest();

        // Mock user input for OTP
        ByteArrayInputStream inputStream = new ByteArrayInputStream("123456".getBytes());
        System.setIn(inputStream);

        // Execute pipeline
        List<Account> resultAccounts = signPipeline.executePipeline();

        // Verify the result
        assertEquals(accounts.size(), resultAccounts.size());
        assertEquals("12345", resultAccounts.get(0).name());
        assertEquals("Checking", resultAccounts.get(0).balance());
        assertEquals("67890", resultAccounts.get(1).name());
        assertEquals("Savings", resultAccounts.get(1).balance());
    }

    @Test
    void testExecutePipeline_LoginRequestFailure() throws IOException, RequestProcessingException {
        // Mock loginRequest to throw RequestProcessingException
        Mockito.doThrow(RequestProcessingException.class).when(signPipeline).loginRequest();

        // Execute pipeline and expect exception
        assertThrows(RequestProcessingException.class, () -> signPipeline.executePipeline());
    }

    @Test
    void testExecutePipeline_PasswordRequestFailure() throws IOException, RequestProcessingException {
        // Mock loginRequest
        AuthResponse loginResponse = new AuthResponse("token", "flowId");
        Mockito.doReturn(loginResponse).when(signPipeline).loginRequest();

        // Mock passwordRequest to throw IOException
        Mockito.doThrow(IOException.class).when(signPipeline).passwordRequest(any(AuthResponse.class));

        // Execute pipeline and expect exception
        assertThrows(IOException.class, () -> signPipeline.executePipeline());
    }

    @Test
    void testExecutePipeline_OTPRequestFailure() throws IOException, RequestProcessingException {
        // Mock loginRequest
        AuthResponse loginResponse = new AuthResponse("token", "flowId");
        Mockito.doReturn(loginResponse).when(signPipeline).loginRequest();

        // Mock passwordRequest
        AuthResponse passwordResponse = new AuthResponse("token", "flowId");
        Mockito.doReturn(passwordResponse).when(signPipeline).passwordRequest(any(AuthResponse.class));

        // Mock otpRequest to throw RequestProcessingException
        Mockito.doThrow(RequestProcessingException.class).when(signPipeline).otpRequest(any(String.class), any(AuthResponse.class));

        // Mock user input for OTP
        ByteArrayInputStream inputStream = new ByteArrayInputStream("123456\n".getBytes()); // Provide input with newline
        System.setIn(inputStream);

        // Execute pipeline and expect exception
        assertThrows(RequestProcessingException.class, () -> signPipeline.executePipeline());
    }


    @Test
    void testExecutePipeline_AccountDataRequestFailure() throws IOException, RequestProcessingException {
        // Mock loginRequest
        AuthResponse loginResponse = new AuthResponse("token", "flowId");
        Mockito.doReturn(loginResponse).when(signPipeline).loginRequest();

        // Mock passwordRequest
        AuthResponse passwordResponse = new AuthResponse("token", "flowId");
        Mockito.doReturn(passwordResponse).when(signPipeline).passwordRequest(any(AuthResponse.class));

        // Mock otpRequest
        doNothing().when(signPipeline).otpRequest(any(String.class), any(AuthResponse.class));

        // Mock accountDataRequest to return empty list
        Mockito.doReturn(Collections.emptyList()).when(signPipeline).accountDataRequest();

        ByteArrayInputStream inputStream = new ByteArrayInputStream("123456\n".getBytes()); // Provide input with newline
        System.setIn(inputStream);

        // Execute pipeline and expect empty list
        List<Account> resultAccounts = signPipeline.executePipeline();
        assertTrue(resultAccounts.isEmpty());
    }

    @Test
    void testReadOTPFromUser() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("123456".getBytes());
        System.setIn(inputStream);

        String otp = signPipeline.readOTPFromUser();
        assertEquals("123456", otp);
    }

//    @Test
//    void testPasswordRequest() throws IOException, RequestProcessingException {
//        // Given
//        AuthResponse loginResponse = new AuthResponse("token", "flowId");
//        AuthResponse expectedResponse = new AuthResponse("token", "flowId");
//        when(loginSubmitRequestProcessor.postRequest(any(PasswordSubmitRequest.class), any(TypeReference.class)))
//                .thenReturn(expectedResponse);
//
//        // When
//        AuthResponse actualResponse = signPipeline.passwordRequest(loginResponse);
//
//        // Then
//        assertEquals(expectedResponse, actualResponse);
//    }
}