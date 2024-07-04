package bank;

import bank.pekao.PekaoBankService;
import bank.pkobp.PKOBPBankService;
import bank.pkobp.pkobpManual.PKOLoginAccounts;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankApplication {

    private static final Logger logger = Logger.getLogger(BankApplication.class.getName());

    public static void main(String[] args) throws IOException {
        logger.log(Level.INFO, "START LOG IN PROCESS FOR PKO PB");
        final var pkobpBankService = new PKOBPBankService();
        pkobpBankService.performLogin();
        logger.log(Level.INFO, "FINISH LOG IN PROCESS FOR PKO PB");

        logger.log(Level.INFO, "START LOG IN PROCESS FOR PEKAO");
        final var scraper = new PekaoBankService();
        try {
            scraper.performLogin();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An exception occurred during login", e);
        }
        logger.log(Level.INFO, "FINISH LOG IN PROCESS FOR PEKAO");

        logger.log(Level.INFO, "START LOG IN PROCESS FOR PKO PB");
        final var pKOLoginAccounts = new PKOLoginAccounts();
        pKOLoginAccounts.loginAndFetchAccounts();
        logger.log(Level.INFO, "FINISH LOG IN PROCESS FOR PKO PB");
    }

}
