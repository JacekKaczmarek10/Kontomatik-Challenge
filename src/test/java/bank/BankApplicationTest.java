package bank;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankApplicationTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Mock
    private BankLoginFacade mockBankLoginFacade;

    @BeforeEach
    void setup() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Nested
    class StartTest{
        @Test
        void testWithPKOBPChoice() {
            final var simulatedInput = "PKO BP\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(testIn);
            final var bankApplication = new BankApplication(mockBankLoginFacade);

            bankApplication.start();

            verify(mockBankLoginFacade, times(1)).determineBankType("PKO BP");
            assertThat(testOut.toString()).contains("Choose your bank (PKO BP or PEKAO):");
        }

        @Test
        void testWithPEKAOChoice() {
            final var simulatedInput = "PEKAO\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(testIn);
            final var bankApplication = new BankApplication(mockBankLoginFacade);

            bankApplication.start();

            verify(mockBankLoginFacade, times(1)).determineBankType("PEKAO");
            assertThat(testOut.toString()).contains("Choose your bank (PKO BP or PEKAO):");
        }

        @Test
        void testWithInvalidChoice() {
            final var simulatedInput = "InvalidBank\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(testIn);
            final var bankApplication = new BankApplication(mockBankLoginFacade);

            bankApplication.start();

            verify(mockBankLoginFacade, never()).performLoginAndGetAccountData(any(), anyString());
            assertThat(testOut.toString()).contains("Choose your bank (PKO BP or PEKAO):");
        }

        @Test
        void testIOException() {
            final var simulatedInput = "PEKAO\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(new InputStreamThrowingIOException());
            final var bankApplication = new BankApplication(mockBankLoginFacade);

            bankApplication.start();

            verify(mockBankLoginFacade, never()).determineBankType(anyString());
            assertThat(testOut.toString()).contains("Choose your bank (PKO BP or PEKAO):");
        }

        private static class InputStreamThrowingIOException extends InputStream {
            @Override
            public int read() throws IOException {
                throw new IOException("IO Exception");
            }
        }
    }

    @Nested
    class MainTest {

        @Test
        void testStarted() {
            final var simulatedInput = "\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(testIn);

            assertDoesNotThrow(() -> BankApplication.main(new String[]{}));
            assertThat(testOut.toString()).contains("Choose your bank (PKO BP or PEKAO):");
        }
    }
}