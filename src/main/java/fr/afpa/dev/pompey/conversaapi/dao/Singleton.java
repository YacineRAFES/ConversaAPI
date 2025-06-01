package fr.afpa.dev.pompey.conversaapi.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static fr.afpa.dev.pompey.conversaapi.utilitaires.Config.*;

public class Singleton {

    private static Connection connection;

    private static final Map<String, Connection> connections = new HashMap<>();

    private Singleton(Properties props) {
//        try {
//            Class.forName(getJDBC_DRIVER());
//            String url = getJDBC_URL();
//            String user = props.getProperty("jdbc.username");
//            String password = props.getProperty("jdbc.password");
//
//            connection = DriverManager.getConnection(url, user, password);
//        } catch (SQLException e) {
//            throw new RuntimeException("Erreur lors de la connexion à la base de données", e);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    // Méthode qui permet de retourner une instance de connexion à la base de données
    public static Connection getInstanceDB(String role) {
        return connections.computeIfAbsent(role.toLowerCase(), r -> {
            Properties propsRole = switch (r) {
                case "utilisateur" -> utilisateur();
                case "moderateur" -> moderateur();
                case "superadmin" -> superadmin();
                default -> throw new IllegalArgumentException("Rôle inconnu : " + role);
            };
            try {
                Class.forName(getJDBC_DRIVER());
                return DriverManager.getConnection(getJDBC_URL(),
                        propsRole.getProperty("jdbc.username"),
                        propsRole.getProperty("jdbc.password"));
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException("Erreur lors de la connexion à la base de données pour le rôle : " + r, e);
            }
        });
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
