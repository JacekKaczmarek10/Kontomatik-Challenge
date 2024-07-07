package bank.pkobp.request_pipelines;

import bank.pkobp.entity.UserCredentials;
import bank.pkobp.request_processors.GetAccountsInfoRequestProcessor;
import bank.pkobp.request_processors.LoginSubmitRequestProcessor;
import bank.pkobp.request_processors.OTPSubmitRequestProcessor;
import bank.pkobp.request_processors.PasswordSubmitRequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

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

    @Mock
    private BufferedReader bufferedReader;

    private PKOBPSignPipeline signPipeline;

    @BeforeEach
    void setUp() {
        signPipeline = new PKOBPSignPipeline(new UserCredentials("username", "password"));
    }

//    @Test
//    void testExecutePipeline() throws IOException, RequestProcessingException {
//        LoginResponse loginResponse = new LoginResponse("sampleSessionId", "user123");
//
//        List<Account> mockAccounts = List.of(
//                new Account("name1", "1000.0"),
//                new Account("name2", "2000.0")
//        );
//
//        // Mock behaviors for LoginSubmitRequestProcessor
//        when(loginSubmitRequestProcessor.postRequest(any(LoginSubmitRequest.class), any(TypeReference.class)))
//                .thenReturn(loginResponse);
//
//        // Mock behaviors for PasswordSubmitRequestProcessor
//        when(passwordSubmitRequestProcessor.postRequest(any(PasswordSubmitRequest.class), any(TypeReference.class)))
//                .thenReturn(loginResponse);
//
//        when(getAccountsInfoRequestProcessor.postRequest(any(String.class), any(TypeReference.class)))
//                .thenReturn(mockAccounts);
//
//        List<Account> resultAccounts = signPipeline.executePipeline();
//
//        ByteArrayInputStream testIn = new ByteArrayInputStream("123456\n".getBytes());
//        System.setIn(testIn);
//
//        // Assertions
//        assertEquals(2, resultAccounts.size());
//        assertEquals("name1", resultAccounts.getFirst().name());
//        assertEquals("1000.0", resultAccounts.get(0).balance());
//        assertEquals("name2", resultAccounts.get(1).name());
//        assertEquals("2000.0", resultAccounts.get(1).balance());
//    }
}
