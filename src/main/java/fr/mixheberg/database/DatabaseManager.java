package fr.mixheberg.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.mixheberg.Main;
import fr.mixheberg.config.ConfigLoader;

import javax.sql.DataSource;

public class DatabaseManager {
    private static HikariDataSource dataSource;

    public static void init() {
        HikariConfig config = new HikariConfig();
        ConfigLoader configFile = Main.getConfig();

        config.setJdbcUrl("jdbc:sqlite:" + configFile.getProperty("db.name"));
        config.setDriverClassName("org.sqlite.JDBC");
        config.setPoolName("MixBotPool");

        config.setMaximumPoolSize(Integer.parseInt(configFile.getProperty("hikari.maxCon")));
        config.setMinimumIdle(1);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);

        config.setConnectionTimeout(10000);
        config.setValidationTimeout(3000);

        config.setConnectionTestQuery("SELECT 1;");

        config.addDataSourceProperty("journal_mode", "WAL");
        config.addDataSourceProperty("cache_size", "5000");
        config.addDataSourceProperty("synchronous", "NORMAL");

        dataSource = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void close() {
        if(dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
