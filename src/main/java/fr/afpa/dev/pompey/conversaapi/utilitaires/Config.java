package fr.afpa.dev.pompey.conversaapi.utilitaires;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe de configuration pour charger les propriétés de l'application.
 *
 * @author Yacine RAFES
 */
public class Config {
    private static final Properties props = new Properties();
    static {
        String pathconf = "conf.properties";
        try(InputStream input = Config.class.getClassLoader().getResourceAsStream(pathconf)) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static Properties chargerFichier(String nomFichier) {
        Properties prop = new Properties();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(nomFichier)) {
            if (input == null) {
                throw new RuntimeException("Fichier n'existe pas : " + nomFichier);
            }
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du fichier : " + nomFichier, e);
        }
        return prop;
    }

    public static Properties utilisateur() {
        return chargerFichier("configUser.properties");
    }

    public static Properties moderateur() {
        return chargerFichier("configModo.properties");
    }

    public static Properties superadmin() {
        return chargerFichier("configSuperAdmin.properties");
    }

    /**
     * Méthode pour récupérer la clé secrète poivre pour le hashage de mot de passe.
     *
     * @return la clé secrète pour le pepper
     */
    public static String getSecretPepper() {
        return props.getProperty("PEPPER_SECRET");
    }

    /**
     * Méthode pour récupérer le driver de JDBC.
     *
     * @return la clé pour le driver de JDBC
     */
    public static String getJDBC_DRIVER() {
        return props.getProperty("jdbc.driver");
    }

    /**
     * Méthode pour récupérer l'URL de JDBC.
     *
     * @return la clé pour l'url de la base de données
     */
    public static String getJDBC_URL() {
        return props.getProperty("jdbc.url");
    }

    /**
     * Méthode pour récupérer le nom d'utilisateur de JDBC.
     *
     * @return la clé pour le nom d'utilisateur
     */
    public static String getJDBC_USER() {
        return props.getProperty("jdbc.username");
    }

    /**
     * Méthode pour récupérer le mot de passe de JDBC.
     *
     * @return la clé pour le mot de passe
     */
    public static String getJDBC_PASSWORD() {
        return props.getProperty("jdbc.password");
    }

    /**
     * Méthode pour récupérer la clé privée.
     *
     * @return la clé privée
     */
    public static String getCLE_PRIVEE() {
        return props.getProperty("CLE_PRIVEE");
    }

    /**
     * Méthode pour récupérer la clé publique.
     *
     * @return la clé publique
     */
    public static String getCLE_PUBLIQUE() {
        return props.getProperty("CLE_PUBLIQUE");
    }

    public static String getJDBC_ROLE_USER() {
        return utilisateur().getProperty("jdbc.username");
    }

    public static String getJDBC_ROLE_USER_PASSWORD() {
        return utilisateur().getProperty("jdbc.password");
    }

    public static String getJDBC_ROLE_MODO_NAME() {
        return moderateur().getProperty("jdbc.username");
    }

    public static String getJDBC_ROLE_MODO_PASSWORD() {
        return moderateur().getProperty("jdbc.password");
    }

    public static String getJDBC_ROLE_SUPERADMIN() {
        return superadmin().getProperty("jdbc.username");
    }

    public static String getJDBC_ROLE_SUPERADMIN_PASSWORD() {
        return superadmin().getProperty("jdbc.password");
    }

}

