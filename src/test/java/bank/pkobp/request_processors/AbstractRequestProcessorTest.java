package bank.pkobp.request_processors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import bank.pkobp.exception.RequestProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
            protected void setSpecificHeaders() {
            }
        };
        requestProcessor.setHttpClient(mockHttpClient);
    }

    @Nested
    class ConvertObjectToJsonTests {

        @Test
        void shouldConvertStringInputToJson() throws JsonProcessingException {
            final var inputString = "test string";

            final var json = requestProcessor.convertObjectToJson(inputString);

            assertThat(json).isEqualTo("test string");
        }

        @Test
        void shouldConvertObjectInputToJson() throws JsonProcessingException {
            final var jsonString = "{\"key\": \"value\"}";

            final var json = requestProcessor.convertObjectToJson(jsonString);

            assertThat(json).isEqualTo("{\"key\": \"value\"}");
        }
    }

    @Nested
    class ParseResponseTests {

        @Test
        void shouldParseJsonResponse() throws JsonProcessingException {
            final var jsonResponse = "{\"key\": \"value\"}";
            final var typeReference = new TypeReference<String>() {
            };

            final var parsedResponse = requestProcessor.parseResponse(jsonResponse, typeReference);

            assertThat(parsedResponse).isEqualTo("{\"key\": \"value\"}");
        }
    }

    @Nested
    class AddHeaderTests {

        @Test
        void shouldAddHeader() {
            final var headerName = "Custom-Header";
            final var headerValue = "header value";

            requestProcessor.addHeader(headerName, headerValue);

            assertThat(requestProcessor.getHeaders()).containsEntry(headerName, headerValue);
        }
    }

    @Nested
    class SetCommonHeadersTests {

        @Test
        void shouldSetCommonHeaders() {
            requestProcessor.setCommonHeaders();

            final var headers = requestProcessor.getHeaders();

            assertThat(headers).containsEntry(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .containsEntry(HttpHeaders.USER_AGENT,
                               "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                .containsEntry(HttpHeaders.ACCEPT, "*/*")
                .containsEntry("Accept-Encoding", "gzip, deflate, br")
                .containsEntry(HttpHeaders.CONNECTION, "keep-alive");
        }
    }

    @Nested
    class PostRequestTests {

        @Test
        void shouldPostRequestAndReturnResponse() throws IOException, RequestProcessingException {
            when(mockHttpClient.execute(any())).thenReturn(mockHttpResponse);
            when(mockHttpResponse.getEntity()).thenReturn(new StringEntity("{\"response\": \"success\"}"));
            final var request = "test request";
            final var responseType = new TypeReference<String>() {
            };

            final var response = requestProcessor.postRequest(request, responseType);

            assertThat(response).isEqualTo("{\"response\": \"success\"}");
        }
    }

    @Nested
    class ProcessRawResponseTests {

        @Test
        void shouldNotThrowExceptionWhenProcessingRawResponse() {

            assertThatCode(() -> requestProcessor.processRawResponse(mockHttpResponse)).doesNotThrowAnyException();
        }
    }
}