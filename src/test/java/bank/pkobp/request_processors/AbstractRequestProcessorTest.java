package bank.pkobp.request_processors;

import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractRequestProcessorTest {

    private AbstractRequestProcessor<String, String> requestProcessor;

    @Mock
    private CloseableHttpClient mockHttpClient;

    @Mock
    private CloseableHttpResponse mockHttpResponse;

    @BeforeEach
    void setUp() {
        requestProcessor = new AbstractRequestProcessor<>() {
            @Override
            protected String getUrl() {
                return "https://example.com/api";
            }

            @Override
            protected void setSpecificHeaders() {}
        };
        requestProcessor.setHttpClient(mockHttpClient);
    }

    @Test
    void testConvertObjectToJson_StringInput() throws JsonProcessingException {
        String inputString = "test string";

        String json = requestProcessor.convertObjectToJson(inputString);

        assertEquals("test string", json);
    }

    @Test
    void testConvertObjectToJson_ObjectInput() throws JsonProcessingException {
        String jsonString = "{\"key\": \"value\"}";

        String json = requestProcessor.convertObjectToJson(jsonString);

        assertEquals("{\"key\": \"value\"}", json);
    }

    @Test
    void testParseResponse() throws JsonProcessingException {
        String jsonResponse = "{\"key\": \"value\"}";
        TypeReference<String> typeReference = new TypeReference<>() {};

        String parsedResponse = requestProcessor.parseResponse(jsonResponse, typeReference);

        assertEquals("{\"key\": \"value\"}", parsedResponse);
    }

    @Test
    void testAddHeader() {
        requestProcessor.addHeader("Custom-Header", "header value");

        assertEquals("header value", requestProcessor.getHeaders().get("Custom-Header"));
    }

    @Test
    void testSetCommonHeaders() {
        requestProcessor.setCommonHeaders();

        assertEquals(MediaType.APPLICATION_JSON, requestProcessor.getHeaders().get(HttpHeaders.CONTENT_TYPE));
        assertEquals("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36", requestProcessor.getHeaders().get(HttpHeaders.USER_AGENT));
        assertEquals("*/*", requestProcessor.getHeaders().get(HttpHeaders.ACCEPT));
        assertEquals("gzip, deflate, br", requestProcessor.getHeaders().get("Accept-Encoding"));
        assertEquals("keep-alive", requestProcessor.getHeaders().get(HttpHeaders.CONNECTION));
    }

    @Test
    void testPostRequest() throws IOException, RequestProcessingException {
        when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.getEntity()).thenReturn(new StringEntity("{\"response\": \"success\"}"));

        String request = "test request";
        TypeReference<String> responseType = new TypeReference<>() {};

        String response = requestProcessor.postRequest(request, responseType);

        assertEquals("{\"response\": \"success\"}", response);
    }

    @Test
    void testProcessRawResponse() {
        assertDoesNotThrow(() -> requestProcessor.processRawResponse(mockHttpResponse));
    }
}