package bank;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class BankApplication {

    private final BankLoginFacade bankLoginFacade;

    public BankApplication(BankLoginFacade bankLoginFacade) {
        this.bankLoginFacade = bankLoginFacade;
    }

    public void start() {
        System.out.print("Choose your bank (PKO BP or PEKAO): ");

        final var reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            final var bankChoice = reader.readLine().trim().toUpperCase();
            bankLoginFacade.determineBankType(bankChoice);
        } catch (IOException e) {
            log.error("An error occurred while reading input: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        BankLoginFacade bankLoginFacade = new BankLoginFacade();
        BankApplication bankApplication = new BankApplication(bankLoginFacade);
        bankApplication.start();
    }
}