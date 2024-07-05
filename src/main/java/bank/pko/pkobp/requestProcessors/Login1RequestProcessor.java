package bank.pko.pkobp.requestProcessors;

import bank.pko.pkobp.LoginContext;
import bank.pko.pkobp.entity.response.LoginResponse;
import bank.pko.pkobp.entity.request.LoginSubmitRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    public String convertObjectToJson(LoginSubmitRequest loginSubmitRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(loginSubmitRequest);
    }

    public LoginResponse parseResponse(String jsonResponse) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, LoginResponse.class);
    }

    public LoginResponse executeRequest(LoginSubmitRequest loginSubmitRequest) throws IOException, InvalidCredentialsException {
        setHeaders();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(LOGIN_URL);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            String json = convertObjectToJson(loginSubmitRequest);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                LoginContext.getInstance().setSessionId(response.getFirstHeader("X-Session-Id").getValue());
                return parseResponse(jsonResponse);
            } catch (NullPointerException e){
                throw new InvalidCredentialsException("Invalid user login");
            }
        }
    }
}