package bank.pkobp.service;

import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.request_pipelines.PKOBPSignPipeline;
import bank.pkobp.utils.AccountResponsePrinter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static bank.pkobp.utils.PropertiesLoader.loadProperties;

@Slf4j
public class PKOBPBankService implements BankService {

    @Override
    public void loginAndGetAccountData() throws IOException, RequestProcessingException {
        final var userCredentials = loadProperties("pkobp-config.properties");

        if (userCredentials == null){
            return;
        }

        final var pkobpSignPipeline = new PKOBPSignPipeline(userCredentials);
        AccountResponsePrinter.displayAccountDetails(pkobpSignPipeline.executePipeline());
    }
}