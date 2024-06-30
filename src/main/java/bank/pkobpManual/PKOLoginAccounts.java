package bank.pkobpManual;

import bank.pkobpManual.entity.Account;
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter session-id: ");
        String sessionId = scanner.nextLine();
        scanner.close();

        String requestBody = "{\"version\":3,\"seq\":12,\"location\":\"\",\"data\":{\"accounts\":{}}}";

        try {
            String response = sendPostRequest(requestBody, Map.of(
                    "Content-Type", "application/json",
                    "x-session-id", sessionId
            ));


            // Parse JSON response and display account details
            List<Account> accountsInfoResponse = parseResponse(response);
            displayAccountDetails(accountsInfoResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String sendPostRequest(String requestBody, Map<String, String> headers) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost("https://www.ipko.pl/ipko3/init");
            setHeaders(request, headers);
            StringEntity entity = new StringEntity(requestBody);
            request.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    private static List<Account> parseResponse(String jsonResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonResponse);

        JsonNode accountsNode = jsonNode.get("response").get("data").get("accounts");

        List<Account> accountsList = new ArrayList<>();

        for (JsonNode accountNode : accountsNode) {
            Account account = mapper.convertValue(accountNode, Account.class);
            accountsList.add(account);
        }
        return accountsList;
    }

    private static void setHeaders(HttpPost request, Map<String, String> headers) {
        headers.forEach(request::setHeader);
    }

    private static void displayAccountDetails(List<Account> responseData) {
        System.out.println("List accounts: ");

        if (responseData != null) {
            responseData.forEach(account -> {
                System.out.println("    Name: " + account.getName());
                System.out.println("    Number: " + account.getAccountNumber().getValue());
                System.out.println("    Balance: " + account.getBalance());
                System.out.println("    -------------------------");
            });
        }
    }
}