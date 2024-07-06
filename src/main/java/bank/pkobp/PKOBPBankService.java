package bank.pkobp;

import bank.pkobp.entity.UserCredentials;
import bank.pkobp.request_pipelines.PKOBPSignPipeline;
import bank.pkobp.utils.AccountResponsePrinter;
import lombok.extern.slf4j.Slf4j;
import static bank.pkobp.utils.PropertiesLoader.loadProperties;

@Slf4j
public class PKOBPBankService {

    public void loginAndGetAccountData() throws Exception {
        UserCredentials userCredentials = loadProperties();

        if (userCredentials == null){
            return;
        }

        PKOBPSignPipeline pkobpSignPipeline = new PKOBPSignPipeline(userCredentials);
        AccountResponsePrinter.displayAccountDetails(pkobpSignPipeline.executePipeline());
    }
}
