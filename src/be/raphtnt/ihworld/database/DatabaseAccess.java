package be.raphtnt.ihworld.database;


import be.raphtnt.ihworld.Main;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAccess {

    private Main main = Main.getPlugin(Main.class);
    private DatabaseCredentials credentials;
    private HikariDataSource hikariDataSource;

    public DatabaseAccess(DatabaseCredentials credentials) {
        this.credentials = credentials;
    }

    public void setupHikariCP() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(main.getConfig().getInt("maxpoolsize"));
        hikariConfig.setJdbcUrl(credentials.toURI());
        hikariConfig.setUsername(credentials.getUser());
        hikariConfig.setPassword(credentials.getPass());
        hikariConfig.setMaxLifetime(600000L);
        hikariConfig.setIdleTimeout(300000L);
        hikariConfig.setLeakDetectionThreshold(300000L);
        hikariConfig.setConnectionTimeout(10000L);

        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void initPool() {
        setupHikariCP();
    }

    public void closePool() {
        this.hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        if(this.hikariDataSource == null) {
            System.out.println("[IHDev] - [BDD] Not connected");
            setupHikariCP();
        }
        return this.hikariDataSource.getConnection();
    }


}
