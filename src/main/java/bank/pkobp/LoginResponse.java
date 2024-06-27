package bank.pkobp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("flow_id")
    private String flowId;

    private String sessionUUID;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFlowId() {
        return flowId;
    }

    @JsonSetter("flow_id")
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getSessionUUID() {
        return sessionUUID;
    }

    public void setSessionUUID(String sessionUUID) {
        this.sessionUUID = sessionUUID;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
            "token='" + token + '\'' +
            ", flowId='" + flowId + '\'' +
            '}';
    }
}
