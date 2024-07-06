package bank.pkobp.utils;

import bank.pkobp.entity.Account;

import java.util.List;

public class AccountResponsePrinter {

    private AccountResponsePrinter(){}

    public static void displayAccountDetails(List<Account> responseData) {
        String accountsOutput = generateAccountDetails(responseData);
        System.out.println(accountsOutput);
    }

    private static String generateAccountDetails(List<Account> responseData) {
        StringBuilder sb = new StringBuilder();
        sb.append("List accounts:\n");

        if (responseData != null && !responseData.isEmpty()) {
            responseData.forEach(account -> {
                sb.append("    Name: ").append(account.name()).append("\n");
                sb.append("    Balance: ").append(account.balance()).append("\n");
                sb.append("    -------------------------\n");
            });
        } else {
            sb.append("    No accounts found\n");
        }

        return sb.toString();
    }
}