package bank.pkobp.request_processors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import bank.pkobp.entity.request.PasswordSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
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
class PasswordSubmitRequestProcessorTest {

    private PasswordSubmitRequestProcessor requestProcessor;

    @Mock
    private CloseableHttpClient mockHttpClient;

    @Mock
    private CloseableHttpResponse mockHttpResponse;

    @BeforeEach
    void setUp() {
        requestProcessor = new PasswordSubmitRequestProcessor();
        requestProcessor.setHttpClient(mockHttpClient);
    }

    @Nested
    class PostRequestTests {

        @Test
        void shouldReturnParsedResponse() throws IOException, RequestProcessingException {
            final var request = new PasswordSubmitRequest(new AuthResponse("token", "flowId"), "password");
            final var jsonResponse =
                "{" + "    \"state_id\": \"one_time_password\"," + "    \"flow_id\": \"flowId\"," + "    \"httpStatus\": 200,"
                    + "    \"token\": \"token\"," + "    \"response\": {" + "        \"fields\": {"
                    + "            \"errors\": \"null\"" + "        }" + "    }" + "}";
            final var typeReference = new TypeReference<AuthResponse>() {
            };
            when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);
            when(mockHttpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));

            final var parsedResponse = requestProcessor.postRequest(request, typeReference);

            assertThat(parsedResponse.flowId()).isEqualTo("flowId");
            assertThat(parsedResponse.token()).isEqualTo("token");
        }
    }
}