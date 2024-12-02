package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws IOException {
        try(InputStream configuration_input = new FileInputStream("src/main/resources/local/config.properties")) {
            Properties configuration = new Properties();
            configuration.load(configuration_input);
            app(configuration);
        }
    }

    public static void app(Properties configuration) {
        while (true) {
            System.out.println("hello..." + configuration.get("app.name"));

            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                System.out.println("App is shutting down");
                throw new RuntimeException(e);
            }

        }
    }
}
