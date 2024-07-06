package bank.pkobp.request_processors;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.request.OTPSubmitRequest;
import bank.pkobp.entity.response.LoginResponse;

public class OTPSubmitRequestProcessor extends AbstractRequestProcessor<OTPSubmitRequest, LoginResponse> {

    private static final String LOGIN_URL = "https://www.ipko.pl/ipko3/login";

    public OTPSubmitRequestProcessor() {
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