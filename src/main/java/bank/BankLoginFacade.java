package bank;

import bank.pekao.PekaoBankService;
import bank.pkobp.service.BankService;
import bank.pkobp.service.PKOBPBankService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BankLoginFacade {
    private static final String PKOBP = "PKO BP";
    private static final String PEKAO = "PEKAO";

    public void loginAndGetAccountData(String bankChoice) {
        final var bankService = switch (bankChoice) {
            case PKOBP -> new PKOBPBankService();
            case PEKAO -> new PekaoBankService();
            default -> {
                log.error("Invalid bank choice: {}", bankChoice);
                yield null;
            }
        };
        if (bankService == null) {
            return;
        }
        loginAndGetAccountData(bankService, bankChoice);
    }

    private void loginAndGetAccountData(BankService bankService, String bankName) {
        log.info("START LOG IN PROCESS FOR {}", bankName);
        try {
            bankService.loginAndGetAccountData();
        } catch (Exception e) {
            log.error("An exception occurred during login to {}: {}", bankName, e.getMessage(), e);
        }
        log.info("FINISH LOG IN PROCESS FOR {}", bankName);
    }
}