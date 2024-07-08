package bank.pkobp.request_processors;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.request.PasswordSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.InvalidCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PasswordSubmitRequestProcessor extends AbstractRequestProcessor<PasswordSubmitRequest, AuthResponse> {

    public PasswordSubmitRequestProcessor() {
        super();
    }

    @Override
    protected String getUrl() {
        return "https://www.ipko.pl/ipko3/login";
    }

    @Override
    protected void setSpecificHeaders() {
        addHeader("x-session-id", SessionContext.getInstance().getSessionId());
    }

    @Override
    protected void processRawJsonResponse(String jsonResponse) throws InvalidCredentialsException, IOException {
        final var objectMapper = new ObjectMapper();
        final var jsonNode = objectMapper.readTree(jsonResponse);

        final var errorsNode = jsonNode.get("response").get("fields").get("errors");
        final var str = errorsNode.asText();
        if (!"null".equals(str)) {
            throw new InvalidCredentialsException("Invalid user password");
        }
    }
}