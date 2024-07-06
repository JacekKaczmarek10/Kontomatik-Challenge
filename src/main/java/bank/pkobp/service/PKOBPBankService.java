package bank.pkobp.service;

import bank.pkobp.entity.UserCredentials;
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
        UserCredentials userCredentials = loadProperties();

        if (userCredentials == null){
            return;
        }

        PKOBPSignPipeline pkobpSignPipeline = new PKOBPSignPipeline(userCredentials);
        AccountResponsePrinter.displayAccountDetails(pkobpSignPipeline.executePipeline());
    }
}
