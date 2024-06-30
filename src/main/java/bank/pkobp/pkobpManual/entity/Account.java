package bank.pkobp.pkobpManual.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    private String name;
    private AccountNumber accountNumber;
    private String balance;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountNumber {
        private String value;
        private String format;
    }
}
