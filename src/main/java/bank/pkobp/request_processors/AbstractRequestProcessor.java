package bank.pkobp.request_processors;

import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequestProcessor<R, S> {

    private static final Logger log = LoggerFactory.getLogger(AbstractRequestProcessor.class);
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
        addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        addHeader(HttpHeaders.ACCEPT, ContentType.WILDCARD.toString());
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
        headers.forEach(httpPost::setHeader);

        String json = convertObjectToJson(request);
        httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String rawResponse = EntityUtils.toString(response.getEntity());
            processRawResponse(response);
            processRawJsonResponse(rawResponse);
            return parseResponse(rawResponse, responseType);
        } catch (IOException | ParseException e) {
            log.error("Error executing POST request: {}", e.getMessage());
            throw new RequestProcessingException("Error processing POST request");
        }
    }

    protected void processRawResponse(CloseableHttpResponse rawResponse) throws RequestProcessingException, IOException, ParseException {}
    protected void processRawJsonResponse(String jsonResponse) throws RequestProcessingException, IOException {}
}