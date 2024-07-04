package bank.pkobp.pkobpManual;

import bank.pkobp.pkobpManual.entity.Account;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PKOLoginAccounts {

    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    public PKOLoginAccounts() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
    }

    public void loginAndFetchAccounts() {
        final var scanner = new Scanner(System.in);
        System.out.print("Enter session-id: ");
        final var sessionId = scanner.nextLine();
        scanner.close();

        final var requestBody = "{\"version\":3,\"seq\":12,\"location\":\"\",\"data\":{\"accounts\":{}}}";

        try {
            final var response = sendPostRequest(requestBody, Map.of(
                "Content-Type", "application/json",
                "x-session-id", sessionId
            ));

            final var accountsInfoResponse = parseResponse(response);
            displayAccountDetails(accountsInfoResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendPostRequest(final String requestBody, final Map<String, String> headers) throws Exception {
        final var request = new HttpPost("https://www.ipko.pl/ipko3/init");
        setHeaders(request, headers);
        final var entity = new StringEntity(requestBody);
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    public List<Account> parseResponse(String jsonResponse) throws Exception {
        final var jsonNode = objectMapper.readTree(jsonResponse);
        final var accountsNode = jsonNode.get("response").get("data").get("accounts");

        List<Account> accountsList = new ArrayList<>();

        for (final var accountNode : accountsNode) {
            final var account = objectMapper.convertValue(accountNode, Account.class);
            accountsList.add(account);
        }
        return accountsList;
    }

    private void setHeaders(HttpPost request, Map<String, String> headers) {
        headers.forEach(request::setHeader);
    }

    public void displayAccountDetails(List<Account> responseData) {
        System.out.println("List accounts: ");

        if (responseData != null) {
            responseData.forEach(account -> {
                System.out.println("    Name: " + account.getName());
                System.out.println("    Balance: " + account.getBalance());
                System.out.println("    -------------------------");
            });
        }
    }
}
