package fr.mixheberg.database;

import fr.mixheberg.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class DatabaseInitializer {
    public DatabaseInitializer(Connection connection) {
        //checkDatabase();
        checkTables(connection);
    }

    private static void checkDatabase() {
        final String DATABASE = Main.getConfig().getProperty("db.name") + ".sql";
        File databaseFile = new File(DATABASE);

        if(!databaseFile.exists()) {
            try {
                Files.createFile(Path.of(DATABASE));
                System.out.println("The database file was created successfully.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("The database file correctly initialized.");
        }
    }

    private static void checkTables(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticket_config (" +
                    "   id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "   guild_id INTEGER NOT NULL," +
                    "   category_id INTEGER," +
                    "   log_channel_id INTEGER," +
                    "   staff_roles INTEGER" +
                    ");");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tickets (" +
                    "   id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "   guild_id INTEGER NOT NULL," +
                    "   channel_id INTEGER NOT NULL," +
                    "   ticket_owner INTEGER NOT NULL" +
                    ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
