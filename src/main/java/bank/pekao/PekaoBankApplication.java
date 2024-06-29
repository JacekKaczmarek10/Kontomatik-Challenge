package bank.pekao;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PekaoBankApplication {

    private static final Logger logger = Logger.getLogger(PekaoBankApplication.class.getName());

    public static void main(String[] args) {
        PekaoBankService scraper = new PekaoBankService();
        try {
            scraper.performLogin();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An exception occurred during login", e);
        }
    }
}
