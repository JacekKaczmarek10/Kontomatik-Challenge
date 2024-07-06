package bank;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class BankApplication {

    public static void main(String[] args) {
        final var reader = new BufferedReader(new InputStreamReader(System.in));
        final var bankLoginFacade = new BankLoginFacade();

        System.out.print("Choose your bank (PKO BP or PEKAO): ");
        try {
            final var bankChoice = reader.readLine().trim().toUpperCase();
            bankLoginFacade.loginAndGetAccountData(bankChoice);
        } catch (IOException e) {
            log.error("An error occurred while reading input: {0}", e);
        }
    }
}