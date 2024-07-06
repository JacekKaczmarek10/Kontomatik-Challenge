package bank.pkobp.request_processors;

import bank.pkobp.entity.request.PasswordSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @Test
    void testPostRequest() throws IOException, RequestProcessingException {
        PasswordSubmitRequest request = new PasswordSubmitRequest(new AuthResponse("token", "flowId"), "password");

        String jsonResponse = "{" +
                "    \"state_id\": \"one_time_password\"," +
                "    \"flow_id\": \"flowId\"," +
                "    \"httpStatus\": 200," +
                "    \"token\": \"token\"" +
                "}";
        TypeReference<AuthResponse> typeReference = new TypeReference<>() {};

        when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));

        AuthResponse parsedResponse = requestProcessor.postRequest(request, typeReference);

        assertEquals("flowId", parsedResponse.flowId());
        assertEquals("token", parsedResponse.token());
    }
}
