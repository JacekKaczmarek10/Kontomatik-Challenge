package bank.pkobp.utils;

import bank.pkobp.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountResponsePrinterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Nested
    class DisplayAccountDetailsTests {

        @Test
        void shouldPrintDetailsForNormalList() {
            List<Account> accounts = Arrays.asList(
                    new Account("John Doe", "1000.00"),
                    new Account("Jane Smith", "2500.50")
            );

            AccountResponsePrinter.displayAccountDetails(accounts);

            String printedOutput = outContent.toString();
            assertThat(printedOutput)
                    .contains("List accounts:")
                    .contains("Name: John Doe")
                    .contains("Balance: 1000.00")
                    .contains("Name: Jane Smith")
                    .contains("Balance: 2500.50");
        }

        @Test
        void shouldPrintNoAccountsFoundForEmptyList() {
            List<Account> accounts = Collections.emptyList();

            AccountResponsePrinter.displayAccountDetails(accounts);

            assertThat(outContent.toString()).contains("List accounts:");
            assertThat(outContent.toString()).contains("No accounts found");
        }

        @Test
        void shouldPrintNoAccountsFoundForNullList() {
            AccountResponsePrinter.displayAccountDetails(null);

            assertThat(outContent.toString()).contains("List accounts:");
            assertThat(outContent.toString()).contains("No accounts found");
        }
    }
}