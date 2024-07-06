package bank.pkobp.request_processors;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.Account;
import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAccountsInfoRequestProcessorTest {

    private GetAccountsInfoRequestProcessor requestProcessor;

    @Mock
    private CloseableHttpClient mockHttpClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CloseableHttpResponse mockHttpResponse;

    @BeforeEach
    void setUp() {
        SessionContext.getInstance().setSessionId("mocked-session-id");
        requestProcessor = new GetAccountsInfoRequestProcessor();
        requestProcessor.setHttpClient(mockHttpClient);
        requestProcessor.setObjectMapper(objectMapper);
    }

    @Test
    void testConvertObjectToJson() throws JsonProcessingException {
        String inputString = "test string";

        String json = requestProcessor.convertObjectToJson(inputString);

        assertEquals("test string", json);
    }

    @Test
    void testPostRequest() throws IOException, RequestProcessingException {
        String requestObject = "{\"some\": \"request\"}";
        String jsonResponse = "{\"response\": {\"data\": {\"accounts\": [{\"name\": \"name\", \"balance\": 100.0}]}}}";
        TypeReference<List<Account>> typeReference = new TypeReference<>() {};

        when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));

        List<Account> parsedResponse = requestProcessor.postRequest(requestObject, typeReference);
        assertEquals(1, parsedResponse.size());
        assertEquals("name", parsedResponse.getFirst().name());
        assertEquals("100.0", parsedResponse.getFirst().balance());
    }

    @Test
    void testSetSpecificHeaders() {
        requestProcessor.setSpecificHeaders();

        assertEquals("mocked-session-id", requestProcessor.getHeaders().get("x-session-id"));
    }
}
