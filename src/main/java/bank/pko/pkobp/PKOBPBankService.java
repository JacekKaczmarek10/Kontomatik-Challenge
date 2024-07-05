package bank.pko.pkobp;

import bank.pko.pkobp.entity.UserCredentials;
import bank.pko.pkobp.entity.request.LoginSubmitRequest;
import bank.pko.pkobp.entity.request.OTPSubmitRequest;
import bank.pko.pkobp.entity.request.PasswordSubmitRequest;
import bank.pko.pkobp.entity.response.LoginResponse;
import bank.pko.pkobp.requestProcessors.GetAccountsDataProcessor;
import bank.pko.pkobp.requestProcessors.Login1RequestProcessor;
import bank.pko.pkobp.requestProcessors.Login2RequestProcessor;
import bank.pko.pkobp.requestProcessors.Login3RequestProcessor;
import bank.pko.pkobp.entity.Account;
import org.apache.http.auth.InvalidCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class PKOBPBankService {

    private static final Logger logger = LoggerFactory.getLogger(PKOBPBankService.class);

    public void performLogin() throws Exception {
        UserCredentials userCredentials = loadProperties();
        LoginResponse loginResponse;

        if (userCredentials == null){
            return;
        }

        try{
            loginResponse = login1(userCredentials);
        }catch (InvalidCredentialsException e){
            logger.error("An error was occurred while login in: ", e);
            return;
        }

        loginResponse = login2(userCredentials, loginResponse);

        String otpCode = readOTPFromUser();
        login3(otpCode, loginResponse);
        getAccountsData();
    }

    private String readOTPFromUser() throws IOException {
        System.out.print("Enter OTP: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine().trim();
    }

    protected UserCredentials loadProperties() {
        UserCredentials userCredentials = new UserCredentials();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("pkobp-config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Unable to find properties file.");
            }

            Properties properties = new Properties();
            properties.load(input);

            String login = properties.getProperty("login");
            String password = properties.getProperty("password");

            // Check if login or password properties are null or empty
            if (login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Properties file contains empty or null login or password.");
            }

            userCredentials.setLogin(login);
            userCredentials.setPassword(password);

            validateProperties(userCredentials);

        } catch (FileNotFoundException e) {
            logger.error("Properties file not found: {}", e.getMessage());
            return null;
        } catch (IOException e) {
            logger.error("Error loading properties file: {}", e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid properties: {}", e.getMessage());
            return null;
        }

        return userCredentials;
    }

    private void validateProperties(UserCredentials userCredentials) {
        if (userCredentials.getLogin() == null || userCredentials.getLogin().isEmpty()) {
            logger.info("Username is missing in the properties file.");
        }
        if (userCredentials.getPassword() == null || userCredentials.getPassword().isEmpty()) {
            logger.info("Password is missing in the properties file.");
        }
    }

    protected LoginResponse login1(UserCredentials userCredentials) throws IOException, InvalidCredentialsException {
        LoginSubmitRequest loginSubmitRequest = new LoginSubmitRequest(userCredentials.getLogin());
        Login1RequestProcessor login1RequestProcessor = new Login1RequestProcessor();
        return login1RequestProcessor.executeRequest(loginSubmitRequest);
    }

    protected LoginResponse login2(UserCredentials userCredentials, LoginResponse loginResponse) throws IOException {
        PasswordSubmitRequest loginRequest = new PasswordSubmitRequest(loginResponse, userCredentials.getPassword());
        Login2RequestProcessor login2RequestProcessor = new Login2RequestProcessor();
        return login2RequestProcessor.executeRequest(loginRequest);
    }

    protected LoginResponse login3(String otp, LoginResponse loginResponse) throws IOException {
        OTPSubmitRequest loginRequest = new OTPSubmitRequest(loginResponse, otp);
        Login3RequestProcessor login3RequestProcessor = new Login3RequestProcessor();
        LoginResponse loginResponse1 = login3RequestProcessor.executeRequest(loginRequest);
        logger.info("Received login response: {}", loginResponse1);
        return loginResponse1;
    }

    protected void getAccountsData() throws Exception {
        GetAccountsDataProcessor getAccountsDataProcessor = new GetAccountsDataProcessor();
        AccountResponseProcessor accountResponseProcessor = new AccountResponseProcessor();
        List<Account> response = accountResponseProcessor.parseResponse(getAccountsDataProcessor.executeRequest());
        accountResponseProcessor.displayAccountDetails(response);
    }

    public static void main(String[] args) throws Exception {
        PKOBPBankService pkobpBankService = new PKOBPBankService();
        pkobpBankService.performLogin();
    }
}
