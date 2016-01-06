package se.pellpin.conf;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * A Server represent neccessary jdbc properties.
 */
public class Server {
    String id;
    String driver;
    String url;
    String username;
    String password;

    public Server() {
    }

    public Connection getConnection() throws Exception {
        Class.forName(this.driver);
        return DriverManager.getConnection(this.url, this.username, this.password);
    }
}
