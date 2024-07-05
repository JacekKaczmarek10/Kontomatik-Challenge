package bank.pko.pkobp.utils;

import bank.pko.pkobp.PKOBPBankService;
import bank.pko.pkobp.entity.UserCredentials;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertiesLoader {

    private PropertiesLoader(){}

    public static UserCredentials loadProperties() {
        try (InputStream input = PKOBPBankService.class.getClassLoader().getResourceAsStream("pkobp-config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Unable to find properties file.");
            }

            Properties properties = new Properties();
            properties.load(input);

            String login = Objects.requireNonNull(properties.getProperty("login"), "Login is missing or empty.");
            String password = Objects.requireNonNull(properties.getProperty("password"), "Password is missing or empty.");

            UserCredentials userCredentials = new UserCredentials(login.trim(), password.trim());
            validateProperties(userCredentials);

            return userCredentials;

        } catch (FileNotFoundException e) {
            log.error("Properties file not found: {}", e.getMessage());
        } catch (IOException | IllegalArgumentException e) {
            log.error("Error loading/invalid properties: {}", e.getMessage());
        }
        return null;
    }

    private static void validateProperties(UserCredentials userCredentials) {
        if (userCredentials.login().isEmpty()) {
            log.info("Username is missing or empty in the properties file.");
        }
        if (userCredentials.password().isEmpty()) {
            log.info("Password is missing or empty in the properties file.");
        }
    }
}