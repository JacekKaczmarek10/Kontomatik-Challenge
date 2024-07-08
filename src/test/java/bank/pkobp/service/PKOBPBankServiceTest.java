package bank.pkobp.service;

import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.utils.AccountResponsePrinter;
import bank.pkobp.utils.PropertiesLoader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.io.IOException;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PKOBPBankServiceTest {

    @InjectMocks
    private PKOBPBankService pkobpBankService;

    @Nested
    class LoginAndGetAccountDataTest {

        @Test
        void shouldLoginAndGetAccountDataWithNullCredentials() throws IOException, RequestProcessingException {
            try (final var mockedLoader = mockStatic(PropertiesLoader.class)) {
                mockedLoader.when(() -> PropertiesLoader.loadProperties("pkobp-config.properties")).thenReturn(null);

                try (final var mockedPrinter = mockStatic(AccountResponsePrinter.class)) {
                    pkobpBankService.loginAndGetAccountData();

                    mockedPrinter.verifyNoInteractions();
                }
            }
        }
    }
}
