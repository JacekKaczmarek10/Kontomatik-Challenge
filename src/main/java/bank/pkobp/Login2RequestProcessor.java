package bank.pkobp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Login2RequestProcessor {

    private final String LOGIN_URL = "https://www.ipko.pl/ipko3/login";
    private final Map<String, String> headers;
    private final ObjectMapper objectMapper;
    private final LoginResponse loginResponse;

    public Login2RequestProcessor(LoginResponse loginResponse) {
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

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }


    public String processRequest(LoginRequest2 loginRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(loginRequest);
    }

    public String executeRequest(LoginRequest2 loginRequest) throws IOException {
        setHeaders();

        try (final var httpClient = HttpClients.createDefault()) {
            final var httpPost = new HttpPost(LOGIN_URL);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            final var json = processRequest(loginRequest);
            return getResponseAsString(httpClient, httpPost, json);
        }
    }

    public static String getResponseAsString(CloseableHttpClient httpClient, HttpPost httpPost, String json) throws IOException {
        final var entity = new StringEntity(json);
        httpPost.setEntity(entity);

        try (final var response = httpClient.execute(httpPost)) {
            System.out.println("Status code: " + response.getStatusLine().getStatusCode());
            return EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}