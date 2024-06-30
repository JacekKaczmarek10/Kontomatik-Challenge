package bank.pkobp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    private final int version;
    private final int seq;
    private final String location;
    @JsonProperty("state_id")
    private final String stateId;
    private final Data data;
    private final String action;

    public LoginRequest(String login) {
        this.version = 3;
        this.seq = 0;
        this.location = "";
        this.stateId = "login";
        this.data = new Data(login);
        this.action = "submit";
    }

    static class Data {
        private String login;
        private String fingerprint;

        public Data(String login) {
            this.login = login;
            this.fingerprint = "7c3afe9843bfb81eda2343f5271ec1ec";
        }

    }
}