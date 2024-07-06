package bank.pkobp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Account(String name, AccountNumber accountNumber, String balance) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AccountNumber(String value, String format) {}
}
