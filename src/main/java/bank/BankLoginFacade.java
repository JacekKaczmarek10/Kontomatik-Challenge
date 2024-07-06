package bank;

import bank.pekao.PekaoBankService;
import bank.pkobp.PKOBPBankService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class BankLoginFacade {
    private static final String PKOBP = "PKO BP";
    private static final String PEKAO = "PEKAO";

    public void loginToBank(String bankChoice) {
        if (PKOBP.equals(bankChoice)) {
            loginToPKOBP();
        } else if (PEKAO.equals(bankChoice)) {
            loginToPEKAO();
        } else {
            log.error("Invalid bank choice: {}", bankChoice);
        }
    }

    private void loginToPKOBP() {
        log.info("START LOG IN PROCESS FOR " + PKOBP);
        final var pkobpBankService = new PKOBPBankService();
        try {
            pkobpBankService.performLogin();
        } catch (Exception e) {
            log.error("An exception occurred during login to " + PKOBP + ": {0}", e);
        }
        log.info("FINISH LOG IN PROCESS FOR " + PKOBP);
    }

    private void loginToPEKAO() {
        log.info("START LOG IN PROCESS FOR " + PEKAO);
        final var pekaoBankService = new PekaoBankService();
        try {
            pekaoBankService.performLogin();
        } catch (IOException e) {
            log.error("An exception occurred during login to " + PEKAO + ": {0}", e);
        }
        log.info("FINISH LOG IN PROCESS FOR " + PEKAO);
    }
}