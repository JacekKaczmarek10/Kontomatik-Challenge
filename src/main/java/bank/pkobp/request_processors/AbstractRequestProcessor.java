package bank.pkobp.request_processors;

import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHeaders;
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

public abstract class AbstractRequestProcessor<R, S> {

    @Getter
    private final Map<String, String> headers;
    @Setter
    private ObjectMapper objectMapper;
    @Getter
    @Setter
    private CloseableHttpClient httpClient;

    protected AbstractRequestProcessor() {
        this.headers = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
    }

    protected abstract String getUrl();

    protected abstract void setSpecificHeaders();

    protected void setCommonHeaders(){
        addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        addHeader(HttpHeaders.ACCEPT, MediaType.WILDCARD);
        addHeader("Accept-Encoding", "gzip, deflate, br");
        addHeader(HttpHeaders.CONNECTION, "keep-alive");
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String convertObjectToJson(R request) throws JsonProcessingException {
        if (request instanceof String string) {
            return string;
        } else {
            return objectMapper.writeValueAsString(request);
        }
    }

    @SuppressWarnings("unchecked")
    public S parseResponse(String jsonResponse, TypeReference<S> responseType) throws JsonProcessingException {
        if (responseType.getType().equals(String.class)) {
            return (S) jsonResponse;
        } else {
            return objectMapper.readValue(jsonResponse, responseType);
        }
    }

    public S postRequest(R request, TypeReference<S> responseType) throws IOException, RequestProcessingException {
        setCommonHeaders();
        setSpecificHeaders();

        HttpPost httpPost = new HttpPost(getUrl());

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }

        String json = convertObjectToJson(request);

        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String rawResponse = EntityUtils.toString(response.getEntity());
            processRawResponse(response);
            return parseResponse(rawResponse, responseType);
        }
    }

    protected void processRawResponse(CloseableHttpResponse rawResponse) throws RequestProcessingException {}
}