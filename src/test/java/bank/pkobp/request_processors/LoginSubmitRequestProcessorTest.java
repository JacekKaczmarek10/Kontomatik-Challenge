package bank.pkobp.request_processors;

import bank.pkobp.entity.request.LoginSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.InvalidCredentialsException;
import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginSubmitRequestProcessorTest {

    private LoginSubmitRequestProcessor requestProcessor;

    @Mock
    private CloseableHttpClient mockHttpClient;

    @Mock
    private CloseableHttpResponse mockHttpResponse;

    @BeforeEach
    void setUp() {
        requestProcessor = new LoginSubmitRequestProcessor();
        requestProcessor.setHttpClient(mockHttpClient);
    }

    @Test
    void testProcessRawResponse_NullResponse() {
        LoginSubmitRequestProcessor processor = new LoginSubmitRequestProcessor();

        assertThrows(InvalidCredentialsException.class, () ->
                processor.processRawResponse(null));
    }

    @Test
    void testPostRequest() throws IOException, RequestProcessingException {
        LoginSubmitRequest request = new LoginSubmitRequest("login");

        String jsonResponse = "{" +
                "    \"flow_id\": \"flowId\"," +
                "    \"token\": \"token\"" +
                "}";
        TypeReference<AuthResponse> typeReference = new TypeReference<>() {};

        when(mockHttpResponse.getFirstHeader("X-Session-Id")).thenReturn(new MockHeader("X-Session-Id", "sessionId"));
        when(mockHttpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));
        when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);

        AuthResponse parsedResponse = requestProcessor.postRequest(request, typeReference);

        assertEquals("flowId", parsedResponse.flowId());
        assertEquals("token", parsedResponse.token());
    }

    private static class MockHeader implements Header {
        private final String name;
        private final String value;

        MockHeader(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public HeaderElement[] getElements() throws ParseException {
            return new HeaderElement[0];
        }
    }
}
