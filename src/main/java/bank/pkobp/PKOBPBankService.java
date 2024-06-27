package bank.pkobp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PKOBPBankService {

    public void performLogin() throws IOException {
        UserCredentials userCredentials = loadProperties();
        LoginResponse loginResponse = login1(userCredentials);
        login2(userCredentials, loginResponse);
        getAccountsData(loginResponse);
    }

    protected UserCredentials loadProperties() {
        final var properties = new Properties();
        UserCredentials userCredentials = new UserCredentials();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("pkobp-config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find properties file.");
            }
            properties.load(input);
            userCredentials = new UserCredentials();
            userCredentials.setLogin(properties.getProperty("login"));
            userCredentials.setPassword(properties.getProperty("password"));
            validateProperties(userCredentials);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return userCredentials;
    }

    private void validateProperties(UserCredentials userCredentials) {
        if (userCredentials.getLogin() == null || userCredentials.getLogin().isEmpty()) {
            System.err.println("Username is missing in the properties file.");
        }
        if (userCredentials.getPassword() == null || userCredentials.getPassword().isEmpty()) {
            System.err.println("Password is missing in the properties file.");
        }
    }

    protected LoginResponse login1(UserCredentials userCredentials) throws IOException {
        LoginRequest loginRequest = new LoginRequest(userCredentials.getLogin());
        Login1RequestProcessor login1RequestProcessor = new Login1RequestProcessor();
        return login1RequestProcessor.executeRequest(loginRequest);
    }

    protected void login2(UserCredentials userCredentials, LoginResponse loginResponse) throws IOException {
        LoginRequest2 loginRequest = new LoginRequest2(loginResponse, userCredentials.getPassword());
        Login2RequestProcessor login2RequestProcessor = new Login2RequestProcessor(loginResponse);
        String response = login2RequestProcessor.executeRequest(loginRequest);
    }

    protected void getAccountsData(LoginResponse loginResponse) throws IOException {
        GetAccountsDataProcessor getAccountsDataProcessor = new GetAccountsDataProcessor(loginResponse);
        String response = getAccountsDataProcessor.executeRequest();
        System.out.println(response);
    }

    public static void main(String[] args) throws IOException {
        PKOBPBankService pkobpBankService = new PKOBPBankService();
        pkobpBankService.performLogin();
    }
}
