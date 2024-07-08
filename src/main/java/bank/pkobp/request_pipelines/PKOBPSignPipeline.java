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
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Setter
public class PKOBPSignPipeline {

    private UserCredentials userCredentials;
    private LoginSubmitRequestProcessor loginSubmitRequestProcessor;
    private PasswordSubmitRequestProcessor passwordSubmitRequestProcessor;
    private OTPSubmitRequestProcessor otpSubmitRequestProcessor;
    private GetAccountsInfoRequestProcessor getAccountsInfoRequestProcessor;
    private BufferedReader otpReader;

    public PKOBPSignPipeline(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
        this.loginSubmitRequestProcessor = new LoginSubmitRequestProcessor();
        this.passwordSubmitRequestProcessor = new PasswordSubmitRequestProcessor();
        this.otpSubmitRequestProcessor = new OTPSubmitRequestProcessor();
        this.getAccountsInfoRequestProcessor = new GetAccountsInfoRequestProcessor();
        this.otpReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public List<Account> executePipeline() throws IOException, RequestProcessingException {
        var loginResponse = loginRequest();
        loginResponse = passwordRequest(loginResponse);
        otpRequest(readOTPFromUser(), loginResponse);
        return accountDataRequest();
    }

    String readOTPFromUser() throws IOException {
        System.out.print("Enter the one-time password sent to you via SMS: ");
        return otpReader.readLine().trim();
    }

    AuthResponse loginRequest() throws IOException, RequestProcessingException {
        final var loginSubmitRequest = new LoginSubmitRequest(userCredentials.login());
        return loginSubmitRequestProcessor.postRequest(loginSubmitRequest, new TypeReference<>() {});
    }

    AuthResponse passwordRequest(AuthResponse authResponse) throws IOException, RequestProcessingException {
        final var loginRequest = new PasswordSubmitRequest(authResponse, userCredentials.password());
        return passwordSubmitRequestProcessor.postRequest(loginRequest, new TypeReference<>() {});
    }

    void otpRequest(String otp, AuthResponse authResponse) throws IOException, RequestProcessingException {
        final var loginRequest = new OTPSubmitRequest(authResponse, otp);
        otpSubmitRequestProcessor.postRequest(loginRequest, new TypeReference<>() {});
    }

    List<Account> accountDataRequest() throws IOException, RequestProcessingException {
        final var json = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";
        return getAccountsInfoRequestProcessor.postRequest(json, new TypeReference<>() {});
    }
}