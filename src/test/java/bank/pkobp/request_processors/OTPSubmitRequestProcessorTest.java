package bank.pkobp.request_processors;

import bank.pkobp.entity.request.OTPSubmitRequest;
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

    @Test
    void testPostRequest() throws IOException, RequestProcessingException {
        OTPSubmitRequest request = new OTPSubmitRequest(new AuthResponse("token", "flowId"), "otp");

        String jsonResponse = "{" +
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
        TypeReference<AuthResponse> typeReference = new TypeReference<>() {};

        when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));

        AuthResponse parsedResponse = requestProcessor.postRequest(request, typeReference);

        assertEquals("flowId", parsedResponse.flowId());
        assertEquals("token", parsedResponse.token());
    }
}