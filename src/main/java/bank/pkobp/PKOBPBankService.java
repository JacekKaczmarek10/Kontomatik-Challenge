package bank.pkobp;

import bank.pkobp.entity.UserCredentials;
import bank.pkobp.request_pipelines.PKOBPSignPipeline;
import bank.pkobp.utils.AccountResponsePrinter;
import lombok.extern.slf4j.Slf4j;
import static bank.pkobp.utils.PropertiesLoader.loadProperties;

@Slf4j
public class PKOBPBankService {

    public static void main(String[] args) throws Exception {
        PKOBPBankService pkobpBankService = new PKOBPBankService();
        pkobpBankService.performLogin();
    }

    public void performLogin() throws Exception {
        UserCredentials userCredentials = loadProperties();

        if (userCredentials == null){
            return;
        }

        PKOBPSignPipeline pkobpSignPipeline = new PKOBPSignPipeline(userCredentials);
        AccountResponsePrinter.displayAccountDetails(pkobpSignPipeline.executePipeline());
    }
}
