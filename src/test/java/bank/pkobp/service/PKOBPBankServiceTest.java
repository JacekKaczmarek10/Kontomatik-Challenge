package bank.pkobp.service;

import bank.pkobp.entity.Account;
import bank.pkobp.entity.UserCredentials;
import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.request_pipelines.PKOBPSignPipeline;
import bank.pkobp.utils.AccountResponsePrinter;
import bank.pkobp.utils.PropertiesLoader;
import java.io.BufferedReader;
import org.junit.jupiter.api.BeforeEach;
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

    private UserCredentials userCredentials;

    @BeforeEach
    void setUp() {
        userCredentials = new UserCredentials("username", "password");
    }

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
