package bank.pkobp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Login1RequestProcessor {

    private final String LOGIN_URL = "https://www.ipko.pl/ipko3/login";
    private final Map<String, String> headers;
    private final ObjectMapper objectMapper;

    public Login1RequestProcessor() {
        this.headers = new HashMap<>();
        this.objectMapper = new ObjectMapper();
    }

    private void setHeaders() {
        addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        addHeader(HttpHeaders.ACCEPT, "*/*");
        addHeader("Accept-Encoding", "gzip, deflate, br");
        addHeader(HttpHeaders.CONNECTION, "keep-alive");
        addHeader("Sec-Ch-Ua", "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"\n");
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String convertObjectToJson(LoginRequest loginRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(loginRequest);
    }

    public LoginResponse parseResponse(String jsonResponse, Header header) throws JsonProcessingException {
        LoginResponse loginResponse = objectMapper.readValue(jsonResponse, LoginResponse.class);
        loginResponse.setSessionUUID(header.getValue());
        return loginResponse;
    }

    public LoginResponse executeRequest(LoginRequest loginRequest) throws IOException {
        setHeaders();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final var httpPost = new HttpPost(LOGIN_URL);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            final var json = convertObjectToJson(loginRequest);
            final var entity = new StringEntity(json);
            httpPost.setEntity(entity);

            try (final var response = httpClient.execute(httpPost)) {
                System.out.println("Status code: " + response.getStatusLine().getStatusCode());
                final var jsonResponse = EntityUtils.toString(response.getEntity());
                return parseResponse(jsonResponse, response.getFirstHeader("X-Session-Id"));
            }
        }
    }

}