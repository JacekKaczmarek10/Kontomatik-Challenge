package bank.pkobp.utils;

import bank.pkobp.entity.UserCredentials;
import lombok.extern.slf4j.Slf4j;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertiesLoader {

    private PropertiesLoader(){}

    public static UserCredentials loadProperties(String propsFile) {
        return loadProperties(propsFile, Thread.currentThread().getContextClassLoader());
    }

    public static UserCredentials loadProperties(String propsFile, ClassLoader classLoader) {
        try (final var input = classLoader.getResourceAsStream(propsFile)) {
            if (input == null) {
                throw new FileNotFoundException("Unable to find properties file.");
            }

            final var properties = new Properties();
            properties.load(input);

            final var login = Objects.requireNonNull(properties.getProperty("login"), "Login is missing or empty.");
            final var password = Objects.requireNonNull(properties.getProperty("password"), "Password is missing or empty.");

            final var userCredentials = new UserCredentials(login.trim(), password.trim());
            validateProperties(userCredentials);

            return userCredentials;

        } catch (FileNotFoundException e) {
            log.error("Properties file not found: {}", e.getMessage());
        } catch (IOException | IllegalArgumentException e) {
            log.error("Error loading/invalid properties: {}", e.getMessage());
        }
        return null;
    }

    static void validateProperties(UserCredentials userCredentials) {
        if (userCredentials.login().isEmpty()) {
            log.info("Username is missing or empty in the properties file.");
        }
        if (userCredentials.password().isEmpty()) {
            log.info("Password is missing or empty in the properties file.");
        }
    }
}