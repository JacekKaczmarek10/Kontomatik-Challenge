package bank.pkobp.request_processors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.Account;
import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Nested
    class ConvertObjectToJsonTests {

        @Test
        void shouldConvertStringInputToJson() throws JsonProcessingException {
            final var inputString = "test string";

            final var json = requestProcessor.convertObjectToJson(inputString);

            assertThat(json).isEqualTo("test string");
        }
    }

    @Nested
    class PostRequestTests {

        @Test
        void shouldPostRequestAndReturnResponse() throws IOException, RequestProcessingException {
            final var requestObject = "{\"some\": \"request\"}";
            final var jsonResponse = "{\"response\": {\"data\": {\"accounts\": [{\"name\": \"name\", \"balance\": 100.0}]}}}";
            final var typeReference = new TypeReference<List<Account>>() {
            };
            when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);
            when(mockHttpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));

            final var parsedResponse = requestProcessor.postRequest(requestObject, typeReference);

            assertThat(parsedResponse).hasSize(1);
            assertThat(parsedResponse.getFirst().name()).isEqualTo("name");
            assertThat(parsedResponse.getFirst().balance()).isEqualTo("100.0");
        }
    }

    @Nested
    class SetSpecificHeadersTests {

        @Test
        void shouldSetSpecificHeaders() {
            requestProcessor.setSpecificHeaders();

            assertThat(requestProcessor.getHeaders()).containsEntry("x-session-id", "mocked-session-id");
        }
    }
}