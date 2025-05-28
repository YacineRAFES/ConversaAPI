package fr.afpa.dev.pompey.conversaapi.exception;

/**
 * Classe exception personnalisé pour les saisies
 */
public class SaisieException extends Exception {

    /**
     * Construction SaisieException sans message
     */
    public SaisieException() {

    }

    /**
     * Construction SaisieException avec message
     */
    public SaisieException(String message) {
        super(message);
    }
}