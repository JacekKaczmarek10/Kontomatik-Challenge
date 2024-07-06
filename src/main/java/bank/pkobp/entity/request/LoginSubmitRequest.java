package bank.pkobp.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginSubmitRequest(
        @JsonProperty("version") int version,
        @JsonProperty("seq") int seq,
        @JsonProperty("location") String location,
        @JsonProperty("state_id") String stateId,
        @JsonProperty("data") Data data,
        @JsonProperty("action") String action
) {
    public LoginSubmitRequest(String login) {
        this(3, 0, "", "login", new Data(login), "submit");
    }

    public record Data(
            @JsonProperty("login") String login,
            @JsonProperty("fingerprint") String fingerprint
    ) {
        public Data(String login) {
            this(login, "7c3afe9843bfb81eda2343f5271ec1ec");
        }
    }
}