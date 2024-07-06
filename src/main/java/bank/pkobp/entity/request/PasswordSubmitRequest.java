package bank.pkobp.entity.request;

import bank.pkobp.entity.response.LoginResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PasswordSubmitRequest(
        @JsonProperty("version") int version,
        @JsonProperty("seq") int seq,
        @JsonProperty("location") String location,
        @JsonProperty("state_id") String stateId,
        @JsonProperty("flow_id") String flowId,
        @JsonProperty("token") String token,
        @JsonProperty("data") Data data,
        @JsonProperty("action") String action
) {
    public PasswordSubmitRequest(LoginResponse loginResponse, String password) {
        this(3, 1, "", "password", loginResponse.flowId(), loginResponse.token(),
                new Data(password, "LoginPKO", 1), "submit");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
            @JsonProperty("password") String password,
            @JsonProperty("placement") String placement,
            @JsonProperty("placement_page_no") Integer placementPageNo
    ) {
    }
}