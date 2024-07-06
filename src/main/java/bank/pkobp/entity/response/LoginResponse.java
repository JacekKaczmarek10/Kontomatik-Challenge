package bank.pkobp.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginResponse(
        @JsonProperty("token") String token,
        @JsonProperty("flow_id") String flowId
) { }
