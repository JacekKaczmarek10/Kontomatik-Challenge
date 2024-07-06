package bank;

import bank.pekao.PekaoBankService;
import bank.pkobp.PKOBPBankService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class BankLoginFacade {
    private static final String PKOBP = "PKO BP";
    private static final String PEKAO = "PEKAO";

    public void loginAndGetAccountData(String bankChoice) {
        if (PKOBP.equals(bankChoice)) {
            loginAndGetAccountDataPKOBP();
        } else if (PEKAO.equals(bankChoice)) {
            loginAndGetAccountDataPekao();
        } else {
            log.error("Invalid bank choice: {}", bankChoice);
        }
    }

    private void loginAndGetAccountDataPKOBP() {
        log.info("START LOG IN PROCESS FOR " + PKOBP);
        final var pkobpBankService = new PKOBPBankService();
        try {
            pkobpBankService.loginAndGetAccountData();
        } catch (Exception e) {
            log.error("An exception occurred during login to " + PKOBP + ": {0}", e);
        }
        log.info("FINISH LOG IN PROCESS FOR " + PKOBP);
    }

    private void loginAndGetAccountDataPekao() {
        log.info("START LOG IN PROCESS FOR " + PEKAO);
        final var pekaoBankService = new PekaoBankService();
        try {
            pekaoBankService.loginAndGetAccountData();
        } catch (IOException e) {
            log.error("An exception occurred during login to " + PEKAO + ": {0}", e);
        }
        log.info("FINISH LOG IN PROCESS FOR " + PEKAO);
    }
}