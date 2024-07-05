package bank.pko.pkobp.request_pipelines;

import bank.pko.pkobp.entity.Account;
import bank.pko.pkobp.entity.UserCredentials;
import bank.pko.pkobp.entity.request.LoginSubmitRequest;
import bank.pko.pkobp.entity.request.OTPSubmitRequest;
import bank.pko.pkobp.entity.request.PasswordSubmitRequest;
import bank.pko.pkobp.entity.response.LoginResponse;
import bank.pko.pkobp.exception.RequestProcessingException;
import bank.pko.pkobp.request_processors.GetAccountsInfoRequestProcessor;
import bank.pko.pkobp.request_processors.LoginSubmitRequestProcessor;
import bank.pko.pkobp.request_processors.OTPSubmitRequestProcessor;
import bank.pko.pkobp.request_processors.PasswordSubmitRequestProcessor;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class PKOBPSignPipeline {

    private final UserCredentials userCredentials;

    public PKOBPSignPipeline(UserCredentials userCredentials){
        this.userCredentials = userCredentials;
    }

    public List<Account> executePipeline() throws IOException, RequestProcessingException {
        LoginResponse loginResponse = loginRequest();
        loginResponse = passwordRequest(loginResponse);
        otpRequest(readOTPFromUser(), loginResponse);
        return accountDataRequest();
    }

    private String readOTPFromUser() throws IOException {
        System.out.print("Enter OTP: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine().trim();
    }

    protected LoginResponse loginRequest() throws IOException, RequestProcessingException {
        LoginSubmitRequest loginSubmitRequest = new LoginSubmitRequest(userCredentials.login());
        LoginSubmitRequestProcessor loginSubmitRequestProcessor = new LoginSubmitRequestProcessor();
        return loginSubmitRequestProcessor.executeRequest(loginSubmitRequest, new TypeReference<>() {});
    }

    protected LoginResponse passwordRequest(LoginResponse loginResponse) throws IOException, RequestProcessingException {
        PasswordSubmitRequest loginRequest = new PasswordSubmitRequest(loginResponse, userCredentials.password());
        PasswordSubmitRequestProcessor passwordSubmitRequestProcessor = new PasswordSubmitRequestProcessor();
        return passwordSubmitRequestProcessor.executeRequest(loginRequest, new TypeReference<>() {});
    }

    protected void otpRequest(String otp, LoginResponse loginResponse) throws IOException, RequestProcessingException {
        OTPSubmitRequest loginRequest = new OTPSubmitRequest(loginResponse, otp);
        OTPSubmitRequestProcessor otpSubmitRequestProcessor = new OTPSubmitRequestProcessor();
        otpSubmitRequestProcessor.executeRequest(loginRequest, new TypeReference<>() {});
    }

    protected List<Account> accountDataRequest() throws IOException, RequestProcessingException {
        String json = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";
        GetAccountsInfoRequestProcessor getAccountsInfoRequestProcessor = new GetAccountsInfoRequestProcessor();
        return getAccountsInfoRequestProcessor.executeRequest(json, new TypeReference<>() {});
    }
}