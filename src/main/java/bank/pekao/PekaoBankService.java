package bank.pekao;

import bank.pkobp.entity.UserCredentials;
import bank.pkobp.service.BankService;
import bank.pkobp.utils.PropertiesLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PekaoBankService implements BankService {

    private static final String PASSWORD_MASK_URL = "https://www.pekao24.pl/api/authentication/customer/logon/password-mask/get";
    private static final String LOGIN_URL = "https://www.pekao24.pl/api/authentication/customer/logon";

    @Setter
    private UserCredentials userCredentials;

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void loginAndGetAccountData() throws IOException {
        userCredentials = PropertiesLoader.loadProperties("pekao-config.properties");

        if (userCredentials == null) {
            return;
        }

        final var passwordMaskResponse = requestPasswordMask();
        log.info("Password Mask Response: {}", passwordMaskResponse);
        if (passwordMaskResponse != null) {
            String maskedPassword = extractMaskedPassword(parsePasswordMask(passwordMaskResponse));
            login(maskedPassword);
        } else {
            log.error("Incorrect password mask");
        }
    }

    String requestPasswordMask() throws IOException {
        return executePostRequest(PASSWORD_MASK_URL, createPasswordMaskRequestBody());
    }

    String extractMaskedPassword(final String passwordMask) {
        final var maskedPassword = new StringBuilder();
        for (int i = 0; i < passwordMask.length(); i++) {
            if (passwordMask.charAt(i) == '1') {
                maskedPassword.append(userCredentials.password().charAt(i));
            }
        }
        return maskedPassword.toString();
    }

    String parsePasswordMask(final String response) throws IOException {
        final var jsonResponse = objectMapper.readTree(response);
        return jsonResponse.get("passwordMask").asText();
    }

    String executePostRequest(final String url, final String json) throws IOException {
        final var postRequest = new HttpPost(url);
        postRequest.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        postRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON);

        try (final var response = httpClient.execute(postRequest)) {
            return IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        }
    }

    String createPasswordMaskRequestBody() {
        final var requestBody = new HashMap<String, String>();
        requestBody.put("customer", userCredentials.login());
        final var passwordMaskRequestBody = convertMapToJson(requestBody);
        log.info("Password Mask Request: {}", passwordMaskRequestBody);
        return passwordMaskRequestBody;
    }

    void login(final String maskedPassword) throws IOException {
        final var loginResponse = executePostRequest(LOGIN_URL, createLoginRequestBody(maskedPassword));
        log.info("Login Response: {}", loginResponse);
    }

    String createLoginRequestBody(final String maskedPassword) {
        final var requestBody = new HashMap<String, String>();
        requestBody.put("customer", userCredentials.login());
        requestBody.put("password", maskedPassword);
        final var loginRequest = convertMapToJson(requestBody);
        log.info("Login Request: {}", loginRequest);
        return loginRequest;
    }

    String convertMapToJson(final Map<String, String> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            log.error("Error converting map to JSON", e);
            return "{}";
        }
    }

}