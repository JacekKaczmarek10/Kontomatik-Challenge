package bank.pkobp.request_processors;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.request.PasswordSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;

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
}