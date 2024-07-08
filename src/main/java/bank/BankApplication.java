package bank;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class BankApplication {

    public static void main(String[] args) {
        System.out.print("Choose your bank (PKO BP or PEKAO): ");

        final var reader = new BufferedReader(new InputStreamReader(System.in));
        final var bankLoginFacade = new BankLoginFacade();

        try {
            final var bankChoice = reader.readLine().trim().toUpperCase();
            bankLoginFacade.determineBankType(bankChoice);
        } catch (IOException e) {
            log.error("An error occurred while reading input: {0}", e);
        }
    }
}