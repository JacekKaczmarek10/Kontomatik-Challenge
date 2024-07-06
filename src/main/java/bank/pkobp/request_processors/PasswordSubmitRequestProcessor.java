package bank.pkobp.request_processors;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.request.PasswordSubmitRequest;
import bank.pkobp.entity.response.LoginResponse;

public class PasswordSubmitRequestProcessor extends AbstractRequestProcessor<PasswordSubmitRequest, LoginResponse> {

    private static final String LOGIN_URL = "https://www.ipko.pl/ipko3/login";

    public PasswordSubmitRequestProcessor() {
        super();
    }

    @Override
    protected String getUrl() {
        return LOGIN_URL;
    }

    @Override
    protected void setSpecificHeaders() {
        addHeader("x-session-id", SessionContext.getInstance().getSessionId());
    }
}