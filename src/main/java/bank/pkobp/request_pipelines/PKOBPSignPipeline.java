package bank.pkobp.request_pipelines;

import bank.pkobp.entity.Account;
import bank.pkobp.entity.UserCredentials;
import bank.pkobp.entity.request.LoginSubmitRequest;
import bank.pkobp.entity.request.OTPSubmitRequest;
import bank.pkobp.entity.request.PasswordSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.RequestProcessingException;
import bank.pkobp.request_processors.GetAccountsInfoRequestProcessor;
import bank.pkobp.request_processors.LoginSubmitRequestProcessor;
import bank.pkobp.request_processors.OTPSubmitRequestProcessor;
import bank.pkobp.request_processors.PasswordSubmitRequestProcessor;
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
        var loginResponse = loginRequest();
        loginResponse = passwordRequest(loginResponse);
        otpRequest(readOTPFromUser(), loginResponse);
        return accountDataRequest();
    }

    String readOTPFromUser() throws IOException {
        System.out.print("Enter the one-time password sent to you via SMS: ");
        final var reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine().trim();
    }

    AuthResponse loginRequest() throws IOException, RequestProcessingException {
        final var loginSubmitRequest = new LoginSubmitRequest(userCredentials.login());
        final var loginSubmitRequestProcessor = new LoginSubmitRequestProcessor();
        return loginSubmitRequestProcessor.postRequest(loginSubmitRequest, new TypeReference<>() {});
    }

    AuthResponse passwordRequest(AuthResponse authResponse) throws IOException, RequestProcessingException {
        final var loginRequest = new PasswordSubmitRequest(authResponse, userCredentials.password());
        final var passwordSubmitRequestProcessor = new PasswordSubmitRequestProcessor();
        return passwordSubmitRequestProcessor.postRequest(loginRequest, new TypeReference<>() {});
    }

    void otpRequest(String otp, AuthResponse authResponse) throws IOException, RequestProcessingException {
        final var loginRequest = new OTPSubmitRequest(authResponse, otp);
        final var otpSubmitRequestProcessor = new OTPSubmitRequestProcessor();
        otpSubmitRequestProcessor.postRequest(loginRequest, new TypeReference<>() {});
    }

    List<Account> accountDataRequest() throws IOException, RequestProcessingException {
        final var json = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";
        final var getAccountsInfoRequestProcessor = new GetAccountsInfoRequestProcessor();
        return getAccountsInfoRequestProcessor.postRequest(json, new TypeReference<>() {});
    }
}