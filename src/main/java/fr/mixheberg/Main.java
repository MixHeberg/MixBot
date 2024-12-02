package fr.mixheberg;

import fr.mixheberg.config.ConfigLoader;
import fr.mixheberg.database.DatabaseInitializer;
import fr.mixheberg.database.DatabaseManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static ConfigLoader config = new ConfigLoader();
    private static JDA jda;

    public static void main(String[] args) {
        DatabaseManager.init();

        try(Connection connection = DatabaseManager.getDataSource().getConnection()) {
            new DatabaseInitializer(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        startBot();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("The bot is finishing some task before shutdown...");

            DatabaseManager.close();
            if(jda != null) {
                jda.shutdown();
            }

            System.out.println("The bot have finished his shutdown. Goodbye!");
        }));
    }

    public static void startBot() {
        jda = JDABuilder.createDefault(config.getProperty("bot.token"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setAutoReconnect(true)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE)
                .setActivity(Activity.listening("Ecoute les clients"))
                .build();
    }

    public static ConfigLoader getConfig() {
        return config;
    }
}