package fr.mixheberg.config;

import fr.mixheberg.utils.Scanner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigInit {
    public static final String CONFIG_FILE = "config.properties";
    private static Scanner scanner = new Scanner();

    public ConfigInit(Properties configFile) {
        String botToken;
        String databaseName;
        int maxConnection;

        System.out.println("Enter your discord bot token.");
        botToken = scanner.stringScanner();

        System.out.println("Enter the name of your database?");
        databaseName = scanner.stringScanner();

        System.out.println("Enter the max amount of connection to open.");
        maxConnection = scanner.intScanner();

        configFile.setProperty("bot.token", botToken);
        configFile.setProperty("db.name", databaseName);
        configFile.setProperty("hikari.maxCon", String.valueOf(maxConnection));

        try(FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            configFile.store(output, null);
            System.out.println("Configuration saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save configurations: " + e.getMessage());
        }
    }
}
