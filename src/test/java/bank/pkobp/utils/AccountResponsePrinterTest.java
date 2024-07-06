package bank.pkobp.utils;

import bank.pkobp.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AccountResponsePrinterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testDisplayAccountDetailsWithNormalList() {
        List<Account> accounts = Arrays.asList(
                new Account("John Doe", "1000.00"),
                new Account("Jane Smith", "2500.50")
        );

        AccountResponsePrinter.displayAccountDetails(accounts);

        String printedOutput = outContent.toString();

        assertTrue(printedOutput.contains("List accounts:"));
        assertTrue(printedOutput.contains("Name: John Doe"));
        assertTrue(printedOutput.contains("Balance: 1000.00"));
        assertTrue(printedOutput.contains("Name: Jane Smith"));
        assertTrue(printedOutput.contains("Balance: 2500.50"));
    }

    @Test
    void testDisplayAccountDetailsWithEmptyList() {
        List<Account> accounts = Collections.emptyList();

        AccountResponsePrinter.displayAccountDetails(accounts);

        String printedOutput = outContent.toString();

        assertTrue(printedOutput.contains("List accounts:"));
        assertTrue(printedOutput.contains("No accounts found"));
    }

    @Test
    void testDisplayAccountDetailsWithNullList() {
        AccountResponsePrinter.displayAccountDetails(null);

        String printedOutput = outContent.toString();

        assertTrue(printedOutput.contains("List accounts:"));
        assertTrue(printedOutput.contains("No accounts found"));
    }
}
