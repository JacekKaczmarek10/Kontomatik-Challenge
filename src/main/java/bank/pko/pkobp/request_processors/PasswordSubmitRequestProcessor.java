package bank.pko.pkobp.request_processors;

import bank.pko.pkobp.context.SessionContext;
import bank.pko.pkobp.entity.request.PasswordSubmitRequest;
import bank.pko.pkobp.entity.response.LoginResponse;

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