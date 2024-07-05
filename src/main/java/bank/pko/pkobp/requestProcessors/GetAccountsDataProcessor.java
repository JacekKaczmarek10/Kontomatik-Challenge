package bank.pko.pkobp.requestProcessors;

import bank.pko.pkobp.LoginContext;
import bank.pko.pkobp.entity.response.LoginResponse;
import bank.pko.pkobp.entity.request.PasswordSubmitRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetAccountsDataProcessor {

    private final String INIT_URL = "https://www.ipko.pl/ipko3/init";
    private final Map<String, String> headers;
    private final ObjectMapper objectMapper;

    public GetAccountsDataProcessor() {
        this.headers = new HashMap<>();
        this.objectMapper = new ObjectMapper();
    }

    private void setHeaders(){
        addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        addHeader(HttpHeaders.ACCEPT, MediaType.WILDCARD);
        addHeader("Accept-Encoding", "gzip, deflate, br");
        addHeader(HttpHeaders.CONNECTION, "keep-alive");
        addHeader("x-session-id", LoginContext.getInstance().getSessionId());
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String processRequest(PasswordSubmitRequest loginRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(loginRequest);
    }

    public String executeRequest() throws IOException {
        setHeaders();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(INIT_URL);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            String json = "{\"version\":3,\"seq\":9,\"location\":\"\",\"data\":{\"accounts\":{}}}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
