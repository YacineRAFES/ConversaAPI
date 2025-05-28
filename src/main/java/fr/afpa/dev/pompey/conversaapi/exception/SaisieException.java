package fr.afpa.dev.pompey.conversaapi.exception;

/**
 * Classe exception personnalisé pour les saisies
 */
public class SaisieException extends Exception {

    /**
     * Constructeur SaisieException sans message
     */
    public SaisieException() {

    }

    /**
     * Constructeur SaisieException avec message
     */
    public SaisieException(String message) {
        super(message);
    }
}