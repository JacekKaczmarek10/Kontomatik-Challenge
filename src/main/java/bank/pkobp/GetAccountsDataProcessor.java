package bank.pkobp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GetAccountsDataProcessor {

    private final String INIT_URL = "https://www.ipko.pl/ipko3/init";
    private final Map<String, String> headers;
    private final ObjectMapper objectMapper;
    private final LoginResponse loginResponse;

    public GetAccountsDataProcessor(LoginResponse loginResponse) {
        this.headers = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        this.loginResponse = loginResponse;
    }

    private void setHeaders(){
        addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        addHeader(HttpHeaders.ACCEPT, MediaType.WILDCARD);
        addHeader("Accept-Encoding", "gzip, deflate, br");
        addHeader(HttpHeaders.CONNECTION, "keep-alive");
        addHeader("x-session-id", loginResponse.getSessionUUID());
    }

    // Method to add a custom header
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    // Method to get headers
    public Map<String, String> getHeaders() {
        return headers;
    }

    // Method to process the LoginRequest and return JSON
    public String processRequest(LoginRequest2 loginRequest) throws JsonProcessingException {
        // Convert LoginRequest to JSON
        return objectMapper.writeValueAsString(loginRequest);
    }

    // Method to execute the HTTP POST request
    public String executeRequest() throws IOException {
        // Set headers
        setHeaders();

        // Create HttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create HttpPost request
            HttpPost httpPost = new HttpPost(INIT_URL);

            // Set headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            // Set the request body
            String json = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);

            // Execute the request
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // Get the response body
                System.out.println("Status code: " + response.getStatusLine().getStatusCode());
                return EntityUtils.toString(response.getEntity());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
