package bank.pkobp.entity.request;

import bank.pkobp.entity.response.AuthResponse;
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
    public OTPSubmitRequest(AuthResponse authResponse, String otp) {
        this(3, 1, "", "one_time_password", authResponse.flowId(), authResponse.token(), new Data(otp), "submit");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
            @JsonProperty("otp") String otp
    ) {
    }
}