package bank.pko.pkobp.request_processors;

import bank.pko.pkobp.context.SessionContext;
import bank.pko.pkobp.entity.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class GetAccountsInfoRequestProcessor extends AbstractRequestProcessor<String, List<Account>> {

    private static final String INIT_URL = "https://www.ipko.pl/ipko3/init";

    public GetAccountsInfoRequestProcessor() {
        super();
    }

    @Override
    protected String getUrl() {
        return INIT_URL;
    }

    @Override
    protected void setSpecificHeaders() {
        addHeader("x-session-id", SessionContext.getInstance().getSessionId());
    }

    @Override
    public List<Account> parseResponse(String jsonResponse, TypeReference<List<Account>> responseType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        final var jsonNode = objectMapper.readTree(jsonResponse);
        final var accountsNode = jsonNode.get("response").get("data").get("accounts");

        List<Account> accountsList = new ArrayList<>();

        for (final var accountNode : accountsNode) {
            final var account = objectMapper.convertValue(accountNode, Account.class);
            accountsList.add(account);
        }
        return accountsList;
    }
}