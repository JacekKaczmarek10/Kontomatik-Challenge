package bank.pkobp.request_processors;

import bank.pkobp.context.SessionContext;
import bank.pkobp.entity.request.OTPSubmitRequest;
import bank.pkobp.entity.response.AuthResponse;

public class OTPSubmitRequestProcessor extends AbstractRequestProcessor<OTPSubmitRequest, AuthResponse> {

    public OTPSubmitRequestProcessor() {
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