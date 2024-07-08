package bank.pkobp.request_processors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import bank.pkobp.entity.request.LoginSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.InvalidCredentialsException;
import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Nested
    class ProcessRawResponseTests {

        @Test
        void shouldThrowInvalidCredentialsExceptionWhenResponseIsNull() {
            final var processor = new LoginSubmitRequestProcessor();

            assertThatThrownBy(() -> processor.processRawResponse(null)).isInstanceOf(InvalidCredentialsException.class);
        }
    }

    @Nested
    class PostRequestTests {

        @Test
        void shouldReturnParsedResponse() throws IOException, RequestProcessingException {
            final var request = new LoginSubmitRequest("login");
            final var jsonResponse = "{" + "    \"flow_id\": \"flowId\"," + "    \"token\": \"token\"" + "}";
            final var typeReference = new TypeReference<AuthResponse>() {
            };
            when(mockHttpResponse.getFirstHeader("X-Session-Id")).thenReturn(new MockHeader("X-Session-Id", "sessionId"));
            when(mockHttpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));
            when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);

            final var parsedResponse = requestProcessor.postRequest(request, typeReference);

            assertThat(parsedResponse.flowId()).isEqualTo("flowId");
            assertThat(parsedResponse.token()).isEqualTo("token");
        }
    }

    private record MockHeader(String name, String value) implements Header {

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public boolean isSensitive() {
            return false;
        }
    }

}