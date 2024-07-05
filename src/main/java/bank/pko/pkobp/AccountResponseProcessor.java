package bank.pko.pkobp;

import bank.pko.pkobp.entity.Account;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class AccountResponseProcessor {

    private final ObjectMapper objectMapper;

    public AccountResponseProcessor() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Account> parseResponse(String jsonResponse) throws Exception {
        final var jsonNode = objectMapper.readTree(jsonResponse);
        final var accountsNode = jsonNode.get("response").get("data").get("accounts");

        List<Account> accountsList = new ArrayList<>();

        for (final var accountNode : accountsNode) {
            final var account = objectMapper.convertValue(accountNode, Account.class);
            accountsList.add(account);
        }
        return accountsList;
    }

    public void displayAccountDetails(List<Account> responseData) {
        System.out.println("List accounts: ");

        if (responseData != null) {
            responseData.forEach(account -> {
                System.out.println("    Name: " + account.name());
                System.out.println("    Balance: " + account.balance());
                System.out.println("    -------------------------");
            });
        }
    }
}
