package bank;

import bank.pekao.PekaoBankService;
import bank.pko.pkobp.PKOBPBankService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class BankApplication {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Choose your bank (PKO BP or PEKAO): ");
        try {
            String bankChoice = reader.readLine().trim().toUpperCase();

            if ("PKO BP".equals(bankChoice)) {
                log.info("START LOG IN PROCESS FOR PKO PB");
                PKOBPBankService pkobpBankService = new PKOBPBankService();
                try {
                    pkobpBankService.performLogin();
                } catch (Exception e) {
                    log.error("An exception occurred during login to PKO BP: {0}", e);
                }
                log.info("FINISH LOG IN PROCESS FOR PKO PB");
            } else if ("PEKAO".equals(bankChoice)) {
                log.info("START LOG IN PROCESS FOR PEKAO");
                PekaoBankService pekaoBankService = new PekaoBankService();
                try {
                    pekaoBankService.performLogin();
                } catch (IOException e) {
                    log.error("An exception occurred during login to PEKAO: {0}", e);
                }
                log.info("FINISH LOG IN PROCESS FOR PEKAO");
            } else {
                log.error("Invalid bank choice: {}", bankChoice);
            }

        } catch (IOException e) {
            log.error("An error occurred while reading input: {0}", e);
        }
    }
}