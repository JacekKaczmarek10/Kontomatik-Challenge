package bank;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
}