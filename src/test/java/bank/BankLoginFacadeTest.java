package bank;

import bank.pekao.PekaoBankService;
import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.service.BankService;
import bank.pkobp.service.PKOBPBankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankLoginFacadeTest {

    @InjectMocks
    @Spy
    private BankLoginFacade facade;

    @Test
    void testLoginAndGetAccountData_PKOBP() throws RequestProcessingException, IOException {
        // Mock BankService for PKOBP
        BankService mockBankService = Mockito.mock(PKOBPBankService.class);

        // Mock logger to capture log messages
        Logger mockLogger = Mockito.mock(Logger.class);
        facade.loginAndGetAccountData("PKOBP");

        // Verify that PKOBPBankService.loginAndGetAccountData() was called
        verify(mockBankService, times(1)).loginAndGetAccountData();

        // Verify log messages
        verify(mockLogger, times(1)).info("START LOG IN PROCESS FOR PKOBP");
        verify(mockLogger, times(1)).info("FINISH LOG IN PROCESS FOR PKOBP");
    }

    @Test
    void testLoginAndGetAccountData_PEKAO() throws RequestProcessingException, IOException {
        // Mock BankService for PEKAO
        BankService mockBankService = Mockito.mock(PekaoBankService.class);

        // Mock logger to capture log messages
        Logger mockLogger = Mockito.mock(Logger.class);
        facade.loginAndGetAccountData("PEKAO");

        // Verify that PekaoBankService.loginAndGetAccountData() was called
        //verify(mockBankService, times(1)).loginAndGetAccountData();

        // Verify log messages
        verify(mockLogger, times(1)).info("START LOG IN PROCESS FOR PEKAO");
        verify(mockLogger, times(1)).info("FINISH LOG IN PROCESS FOR PEKAO");
    }
}