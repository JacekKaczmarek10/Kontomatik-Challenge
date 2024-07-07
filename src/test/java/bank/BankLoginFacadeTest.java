package bank;

import bank.pekao.PekaoBankService;
import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.service.BankService;
import bank.pkobp.service.PKOBPBankService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @Disabled
    class LoginAndGetAccountData {

        @Test
        void shouldLoginAndGetAccountData_PKOBP() throws RequestProcessingException, IOException {
            BankService mockBankService = Mockito.mock(PKOBPBankService.class);

            Logger mockLogger = Mockito.mock(Logger.class);
            facade.loginAndGetAccountData("PKO BP");

            verify(mockBankService, times(1)).loginAndGetAccountData();

            verify(mockLogger, times(1)).info("START LOG IN PROCESS FOR PKO BP");
            verify(mockLogger, times(1)).info("FINISH LOG IN PROCESS FOR PKO BP");
        }

        @Test
        @Disabled
        void shouldLoginAndGetAccountData_PEKAO() {
            BankService mockBankService = Mockito.mock(PekaoBankService.class);

            Logger mockLogger = Mockito.mock(Logger.class);
            facade.loginAndGetAccountData("PEKAO");

            verify(mockLogger, times(1)).info("START LOG IN PROCESS FOR PEKAO");
            verify(mockLogger, times(1)).info("FINISH LOG IN PROCESS FOR PEKAO");
        }

    }

}