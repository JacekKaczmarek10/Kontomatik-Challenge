package bank.pko.pkobp;

public class LoginContext {

    private static LoginContext instance;
    private String sessionId;

    private LoginContext() {}

    public static synchronized LoginContext getInstance() {
        if (instance == null) {
            instance = new LoginContext();
        }
        return instance;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
