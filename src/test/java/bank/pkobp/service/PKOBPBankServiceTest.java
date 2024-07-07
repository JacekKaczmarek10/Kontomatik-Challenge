package bank.pkobp.service;

import bank.pkobp.entity.Account;
import bank.pkobp.entity.UserCredentials;
import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.request_pipelines.PKOBPSignPipeline;
import bank.pkobp.utils.AccountResponsePrinter;
import bank.pkobp.utils.PropertiesLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PKOBPBankServiceTest {

    @Mock
    private PKOBPSignPipeline mockSignPipeline;

    @Mock
    private AccountResponsePrinter mockAccountResponsePrinter;

    @InjectMocks
    private PKOBPBankService pkobpBankService;

    private UserCredentials userCredentials;

    @BeforeEach
    void setUp() {
        userCredentials = new UserCredentials("username", "password");
    }

    @Test
    void testLoginAndGetAccountDataSuccess() throws IOException, RequestProcessingException {
        try(MockedStatic<PropertiesLoader> mockedStatic = mockStatic(PropertiesLoader.class)) {
            List<Account> mockAccounts = List.of(new Account("12345", "Checking"), new Account("67890", "Savings"));
            when(mockSignPipeline.executePipeline()).thenReturn(mockAccounts);

            // When
            pkobpBankService.loginAndGetAccountData();

            // Then
            verify(mockSignPipeline).executePipeline();
            verify(mockAccountResponsePrinter).displayAccountDetails(mockAccounts);
        }
    }

    @Test
    void testLoginAndGetAccountDataWithNullCredentials() throws IOException, RequestProcessingException {
        try (MockedStatic<PropertiesLoader> mockedStatic = mockStatic(PropertiesLoader.class)) {
            mockedStatic.when(() -> PropertiesLoader.loadProperties(anyString())).thenReturn(null);

            // When
            pkobpBankService.loginAndGetAccountData();

            // Then
            verify(mockSignPipeline, never()).executePipeline();
            //verify(mockAccountResponsePrinter, never()).displayAccountDetails(any());
        }
    }

//    @Test
//    void testLoginAndGetAccountDataIOException() throws IOException, RequestProcessingException {
//        // Given
//        when(mockSignPipeline.executePipeline()).thenThrow(new IOException("Test IOException"));
//
//        // When & Then
//        assertThrows(IOException.class, () -> pkobpBankService.loginAndGetAccountData());
//    }

//    @Test
//    void testLoginAndGetAccountDataRequestProcessingException() throws IOException, RequestProcessingException {
//        // Given
//        when(mockSignPipeline.executePipeline()).thenThrow(new RequestProcessingException("Test RequestProcessingException"));
//
//        // When & Then
//        assertThrows(RequestProcessingException.class, () -> pkobpBankService.loginAndGetAccountData());
//    }
}
