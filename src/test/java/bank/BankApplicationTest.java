package bank;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BankApplicationTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    void testBankApplicationWithValidInputPKOBP() {
        // Simulate user input "PKOBP"
        testIn = new ByteArrayInputStream("PKOBP\n".getBytes());
        System.setIn(testIn);

        // Run BankApplication.main() and capture console output
        BankApplication.main(new String[]{});

        // Verify the output
        String output = testOut.toString();
        assertTrue(output.contains("START LOG IN PROCESS FOR PKOBP"));
        assertTrue(output.contains("FINISH LOG IN PROCESS FOR PKOBP"));

        // Reset System.in for subsequent tests
        System.setIn(systemIn);
    }

    @Test
    void testBankApplicationWithValidInputPEKAO() {
        // Simulate user input "PEKAO"
        testIn = new ByteArrayInputStream("PEKAO\n".getBytes());
        System.setIn(testIn);

        // Run BankApplication.main() and capture console output
        BankApplication.main(new String[]{});

        // Verify the output
        String output = testOut.toString();
        assertTrue(output.contains("START LOG IN PROCESS FOR PEKAO"));
        assertTrue(output.contains("FINISH LOG IN PROCESS FOR PEKAO"));

        // Reset System.in for subsequent tests
        System.setIn(systemIn);
    }

    @Test
    void testBankApplicationWithInvalidInput() {
        // Simulate user input "InvalidBank"
        testIn = new ByteArrayInputStream("InvalidBank\n".getBytes());
        System.setIn(testIn);

        // Run BankApplication.main() and capture console output
        BankApplication.main(new String[]{});

        // Verify the output
        String output = testOut.toString();
        assertTrue(output.contains("Invalid bank choice: INVALIDBANK"));

        // Reset System.in for subsequent tests
        System.setIn(systemIn);
    }

//    @Mock
//    private BufferedReader mockReader;
//
//    @Mock
//    private BankLoginFacade mockBankLoginFacade;
//
//    @InjectMocks
//    private BankApplication bankApplication;
//
//    @Mock
//    private Logger mockLogger;
//
//    @BeforeEach
//    void setUp() {
//        reset(mockLogger);
//    }
//
//    @Test
//    void testIOExceptionLogging() throws IOException {
//        // Given
//        when(mockReader.readLine()).thenThrow(new IOException("Test IOException"));
//
//        // Redirect BufferedReader to the mock
//        System.setIn(new InputStreamReader(mockReader));
//
//        // When
//        BankApplication.main(new String[]{});
//
//        // Then
//        verify(mockLogger).error(anyString(), any(IOException.class));
//    }
}