package bank.pkobp.request_processors;

import bank.pkobp.entity.request.OTPSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OTPSubmitRequestProcessorTest {

    private OTPSubmitRequestProcessor requestProcessor;

    @Mock
    private CloseableHttpClient mockHttpClient;

    @Mock
    private CloseableHttpResponse mockHttpResponse;

    @BeforeEach
    void setUp() {
        requestProcessor = new OTPSubmitRequestProcessor();
        requestProcessor.setHttpClient(mockHttpClient);
    }

    @Nested
    class PostRequestTests {

        @Test
        void shouldReturnParsedResponse() throws IOException, RequestProcessingException {
            final var request = new OTPSubmitRequest(new AuthResponse("token", "flowId"), "otp");
            final var jsonResponse = "{" +
                    "    \"state_id\": \"END\"," +
                    "    \"httpStatus\": 200,\n" +
                    "    \"flow_id\": \"flowId\"," +
                    "    \"token\": \"token\"," +
                    "    \"finished\": true," +
                    "    \"response\": {" +
                    "        \"data\": {" +
                    "            \"login_type\": \"NORMAL\"" +
                    "        }" +
                    "    }" +
                    "}";
            final var typeReference = new TypeReference<AuthResponse>() {};
            when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);
            when(mockHttpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));

            final var parsedResponse = requestProcessor.postRequest(request, typeReference);

            assertThat(parsedResponse.flowId()).isEqualTo("flowId");
            assertThat(parsedResponse.token()).isEqualTo("token");
        }
    }
}