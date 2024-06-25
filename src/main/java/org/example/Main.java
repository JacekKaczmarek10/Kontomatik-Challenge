import org.example.PekaoService;

import java.io.IOException;

public static void main(String[] args) {
    PekaoService scraper = new PekaoService();
    try {
        scraper.performLogin();
    } catch (IOException e) {
        e.printStackTrace();
    }
}