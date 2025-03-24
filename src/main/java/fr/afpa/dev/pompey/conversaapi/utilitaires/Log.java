package fr.afpa.dev.pompey.conversaapi.utilitaires;

import java.util.logging.*;

public class Log {
    private static final Logger LOGGER = Logger.getLogger(Log.class.getName());

    static {
        try {
            // Évite la duplication de logs
            LOGGER.setUseParentHandlers(false);
            // Console Handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO); // Niveau minimum des logs pour la console
            consoleHandler.setFormatter(new SimpleFormatter()); // Formatteur simple
            LOGGER.addHandler(consoleHandler);

            // File Handler (écriture dans un fichier)
            FileHandler fileHandler = new FileHandler("app.log", true); // true pour append
            fileHandler.setLevel(Level.WARNING); // Tous les niveaux enregistrés dans le fichier
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            // Niveau global des logs
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            // En cas d'échec de configuration
            LOGGER.log(Level.SEVERE, "Failed to initialize logger handlers", e);
        }
    }


    public static void info(String message) {
        LOGGER.info(message);
    }
    public static void error(String message) {
        LOGGER.log(Level.SEVERE, message);
    }
    public static void error(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }
}
