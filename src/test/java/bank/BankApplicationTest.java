package bank;

import org.junit.jupiter.api.*;
import org.mockito.*;
import java.io.IOException;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankApplicationTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Mock
    private BankLoginFacade mockBankLoginFacade;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
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
            String simulatedInput = "PKO BP\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(testIn);

            BankApplication bankApplication = new BankApplication(mockBankLoginFacade);
            bankApplication.start();

            verify(mockBankLoginFacade, times(1)).determineBankType("PKO BP");
            assertTrue(testOut.toString().contains("Choose your bank (PKO BP or PEKAO):"));
        }

        @Test
        void testWithPEKAOChoice() {
            String simulatedInput = "PEKAO\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(testIn);

            BankApplication bankApplication = new BankApplication(mockBankLoginFacade);
            bankApplication.start();

            verify(mockBankLoginFacade, times(1)).determineBankType("PEKAO");
            assertTrue(testOut.toString().contains("Choose your bank (PKO BP or PEKAO):"));
        }

        @Test
        void testWithInvalidChoice() {
            String simulatedInput = "InvalidBank\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(testIn);

            BankApplication bankApplication = new BankApplication(mockBankLoginFacade);
            bankApplication.start();

            verify(mockBankLoginFacade, never()).performLoginAndGetAccountData(any(), anyString());
            assertTrue(testOut.toString().contains("Choose your bank (PKO BP or PEKAO):"));
        }

        @Test
        void testIOException() {
            String simulatedInput = "PEKAO\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(new InputStreamThrowingIOException());

            BankApplication bankApplication = new BankApplication(mockBankLoginFacade);
            bankApplication.start();
            verify(mockBankLoginFacade, never()).determineBankType(anyString());
            assertTrue(testOut.toString().contains("Choose your bank (PKO BP or PEKAO):"));
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
            String simulatedInput = "\n";
            testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(testIn);

            assertDoesNotThrow(() -> BankApplication.main(new String[]{}));
            assertTrue(testOut.toString().contains("Choose your bank (PKO BP or PEKAO):"));
        }
    }
}