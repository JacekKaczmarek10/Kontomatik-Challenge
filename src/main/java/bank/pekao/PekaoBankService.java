package bank.pekao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;

public class PekaoBankService {

    private static final String PASSWORD_MASK_URL = "https://www.pekao24.pl/api/authentication/customer/logon/password-mask/get";
    private static final String LOGIN_URL = "https://www.pekao24.pl/api/authentication/customer/logon";

    String username;
    String password;

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final Gson gson = new Gson();

    public void performLogin() throws IOException {
        loadProperties();
        final var passwordMaskResponse = requestPasswordMask();
        System.out.println("Password Mask Response: " + passwordMaskResponse);
        if (passwordMaskResponse != null) {
            String maskedPassword = extractMaskedPassword(parsePasswordMask(passwordMaskResponse));
            login(maskedPassword);
        }
    }

    void loadProperties() {
        final var properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Unable to find properties file.");
                return;
            }
            properties.load(input);
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            validateProperties();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void validateProperties() {
        if (username == null || username.isEmpty()) {
            System.err.println("Username is missing in the properties file.");
        }
        if (password == null || password.isEmpty()) {
            System.err.println("Password is missing in the properties file.");
        }
    }

    protected String requestPasswordMask() throws IOException {
        return executePostRequest(PASSWORD_MASK_URL, createPasswordMaskRequestBody());
    }

    protected String executePostRequest(String url, String json) throws IOException {
        final var postRequest = new HttpPost(url);
        postRequest.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        try (final var response = httpClient.execute(postRequest)) {
            return IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        }
    }

    protected String createPasswordMaskRequestBody() {
        final var requestBody = new HashMap<>();
        requestBody.put("customer", username);
        final var passwordMaskRequestBody = gson.toJson(requestBody);
        System.out.println("Password Mask Request: " + passwordMaskRequestBody);
        return passwordMaskRequestBody;
    }

    protected String parsePasswordMask(String response) {
        final var jsonResponse = gson.fromJson(response, JsonObject.class);
        return jsonResponse.get("passwordMask").getAsString();
    }

    protected String extractMaskedPassword(String passwordMask) {
        final var maskedPassword = new StringBuilder();
        for (int i = 0; i < passwordMask.length(); i++) {
            if (passwordMask.charAt(i) == '1') {
                maskedPassword.append(password.charAt(i));
            }
        }
        return maskedPassword.toString();
    }

    protected void login(String maskedPassword) throws IOException {
        final var loginResponse = executePostRequest(LOGIN_URL, createLoginRequestBody(maskedPassword));
        System.out.println("Login Response: " + loginResponse);
    }

    protected String createLoginRequestBody(String maskedPassword) {
        final var requestBody = new HashMap<>();
        requestBody.put("customer", username);
        requestBody.put("password", maskedPassword);
        final var loginRequest = gson.toJson(requestBody);
        System.out.println("Login Response: " + loginRequest);
        return loginRequest;
    }
}