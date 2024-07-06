package bank.pkobp.request_processors;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.request.LoginSubmitRequest;
import bank.pkobp.entity.response.LoginResponse;
import bank.pkobp.exception.InvalidCredentialsException;
import org.apache.http.client.methods.CloseableHttpResponse;

public class LoginSubmitRequestProcessor extends AbstractRequestProcessor<LoginSubmitRequest, LoginResponse> {

    public LoginSubmitRequestProcessor() {
        super();
    }

    @Override
    protected String getUrl() {
        return "https://www.ipko.pl/ipko3/login";
    }

    @Override
    protected void setSpecificHeaders() {
        addHeader("Sec-Ch-Ua", "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"");
    }

    @Override
    protected void processRawResponse(CloseableHttpResponse response) throws InvalidCredentialsException {
        if (response != null && response.getFirstHeader("X-Session-Id") != null) {
            SessionContext.getInstance().setSessionId(response.getFirstHeader("X-Session-Id").getValue());
        } else {
            throw new InvalidCredentialsException("Invalid user login");
        }
    }
}