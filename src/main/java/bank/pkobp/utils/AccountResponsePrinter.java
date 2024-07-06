package bank.pkobp.utils;

import bank.pkobp.entity.Account;

import java.util.List;

public class AccountResponsePrinter {

    private AccountResponsePrinter(){}

    public static void displayAccountDetails(List<Account> responseData) {
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
