package bank.pkobp.service;

import bank.pkobp.exception.RequestProcessingException;

import java.io.IOException;

public interface BankService {
    void loginAndGetAccountData() throws IOException, RequestProcessingException;
}

