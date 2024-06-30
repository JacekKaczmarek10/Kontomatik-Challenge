package bank.pkobp;

import static bank.pkobp.Login2RequestProcessor.getResponseAsString;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

public class AccountDataProcessor {

    private final String INIT_URL = "https://www.ipko.pl/ipko3/init";
    private final Map<String, String> headers;
    private final ObjectMapper objectMapper;
    private final LoginResponse loginResponse;

    public AccountDataProcessor(LoginResponse loginResponse) {
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

    public String executeRequest() throws IOException {
        setHeaders();

        try (final var httpClient = HttpClients.createDefault()) {
            final var httpPost = new HttpPost(INIT_URL);
            for (final var entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            final var json = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";
            return getResponseAsString(httpClient, httpPost, json);
        }
    }
}
