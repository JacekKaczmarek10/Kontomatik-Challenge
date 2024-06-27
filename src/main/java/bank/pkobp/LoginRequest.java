package bank.pkobp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
    private int version;
    private int seq;
    private String location;
    @JsonProperty("state_id")
    private String stateId;
    private Data data;
    private String action;

    // Constructor with predefined values
    public LoginRequest(String login) {
        this.version = 3;
        this.seq = 0;
        this.location = "";
        this.stateId = "login";
        this.data = new Data(login);
        this.action = "submit";
    }

    // Getters and setters
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // Inner Data class
    static class Data {
        private String login;
        private String fingerprint;

        // Constructor
        public Data(String login) {
            this.login = login;
            this.fingerprint = "7c3afe9843bfb81eda2343f5271ec1ec";
        }

        // Getters and setters
        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }
    }
}