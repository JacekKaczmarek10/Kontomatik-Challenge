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

    // Method to add a custom header
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    // Method to get headers
    public Map<String, String> getHeaders() {
        return headers;
    }

    // Method to convert object to JSON string
    public String convertObjectToJson(LoginRequest loginRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(loginRequest);
    }

    // Method to parse JSON response to object
    public LoginResponse parseResponse(String jsonResponse, Header header) throws JsonProcessingException {
        LoginResponse loginResponse = objectMapper.readValue(jsonResponse, LoginResponse.class);
        loginResponse.setSessionUUID(header.getValue());
        return loginResponse;
    }

    // Method to execute the HTTP POST request
    public LoginResponse executeRequest(LoginRequest loginRequest) throws IOException {
        // Set headers
        setHeaders();

        // Create HttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create HttpPost request
            HttpPost httpPost = new HttpPost(LOGIN_URL);

            // Set headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            // Set the request body
            String json = convertObjectToJson(loginRequest);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);

            // Execute the request
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // Get the response body
                System.out.println("Status code: " + response.getStatusLine().getStatusCode());
                String jsonResponse = EntityUtils.toString(response.getEntity());
                return parseResponse(jsonResponse, response.getFirstHeader("X-Session-Id"));
            }
        }
    }
}