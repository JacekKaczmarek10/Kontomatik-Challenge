package bank.pko.pkobp.entity.request;

import bank.pko.pkobp.entity.response.LoginResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OTPSubmitRequest(
        @JsonProperty("version") int version,
        @JsonProperty("seq") int seq,
        @JsonProperty("location") String location,
        @JsonProperty("state_id") String stateId,
        @JsonProperty("flow_id") String flowId,
        @JsonProperty("token") String token,
        @JsonProperty("data") Data data,
        @JsonProperty("action") String action
) {
    public OTPSubmitRequest(LoginResponse loginResponse, String otp) {
        this(3, 1, "", "one_time_password", loginResponse.flowId(), loginResponse.token(), new Data(otp), "submit");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
            @JsonProperty("otp") String otp
    ) {
    }
}