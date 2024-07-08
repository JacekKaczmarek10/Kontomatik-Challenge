package bank.pkobp.request_processors;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.request.PasswordSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;
import bank.pkobp.exception.InvalidCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.Optional;

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
    protected void processRawResponse(CloseableHttpResponse response) throws InvalidCredentialsException{
        try{
            final var rawResponse = EntityUtils.toString(response.getEntity());
            final var objectMapper = new ObjectMapper();
            final var jsonNode = objectMapper.readTree(rawResponse);
            final var node = Optional.of(jsonNode.get("response").get("fields").get("errors"))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid user password"));
        } catch (IOException | ParseException e){
            throw new InvalidCredentialsException("Invalid user password");
        }
    }
}