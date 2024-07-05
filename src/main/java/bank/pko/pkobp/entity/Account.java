package bank.pko.pkobp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Account(String name, Account.AccountNumber accountNumber, String balance) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AccountNumber(String value, String format) {}
}
