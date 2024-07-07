package bank;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Nested
    @Disabled
    class MainTest {

        @Test
        void shouldBankApplicationWithValidInputPKOBP() {
            testIn = new ByteArrayInputStream("PKO BP\n".getBytes());
            System.setIn(testIn);
            BankApplication.main(new String[]{});

            final var output = testOut.toString();

            assertThat(output).contains("START LOG IN PROCESS FOR PKO BP");
            assertThat(output).contains("FINISH LOG IN PROCESS FOR PKO BP");
        }

        @Test
        void shouldBankApplicationWithValidInputPEKAO() {
            testIn = new ByteArrayInputStream("PEKAO\n".getBytes());
            System.setIn(testIn);
            BankApplication.main(new String[]{});

            final var output = testOut.toString();

            assertThat(output).contains("START LOG IN PROCESS FOR PEKAO");
            assertThat(output).contains("FINISH LOG IN PROCESS FOR PEKAO");
        }

        @Test
        void shouldBankApplicationWithInvalidInput() {
            testIn = new ByteArrayInputStream("InvalidBank\n".getBytes());
            System.setIn(testIn);
            BankApplication.main(new String[]{});

            final var output = testOut.toString();

            assertThat(output).contains("Invalid bank choice: INVALIDBANK");
        }
    }
}
