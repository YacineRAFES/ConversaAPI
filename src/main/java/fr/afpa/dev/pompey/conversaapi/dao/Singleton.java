package fr.afpa.dev.pompey.conversaapi.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Singleton {

    private static final Properties props = new Properties();
    private static Connection connection;
    final String PATHCONF = "conf.properties";

    private Singleton() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PATHCONF)) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Class.forName(props.getProperty("jdbc.driver"));
            String url = props.getProperty("jdbc.url");
            String user = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion à la base de données", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode qui permet de retourner une instance de connexion à la base de données
    public static Connection getInstanceDB() {
        if (getConnection() == null) {
            new Singleton();
        }
        return getConnection();
    }

    // Méthode qui permet de fermer la connexion à la base de données
    public static void closeConnection() {
        try {
            getConnection().close();
            System.out.println("Connexion à la base de données fermée !");
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    // Méthode qui permet de retourner la connexion à la base de données
    private static Connection getConnection() {
        return connection;
    }
}
