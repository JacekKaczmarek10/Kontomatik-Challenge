package bank;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankApplication {

    private static final Logger logger = Logger.getLogger(BankApplication.class.getName());

    public static void main(String[] args) {
        BankService scraper = new BankService();
        try {
            scraper.performLogin();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An exception occurred during login", e);
        }
    }
}
