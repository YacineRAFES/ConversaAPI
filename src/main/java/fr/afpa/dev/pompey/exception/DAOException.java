package fr.afpa.dev.pompey.exception;

public class DAOException extends RuntimeException {
    public DAOException() {

    }
    public DAOException(String message) {
        super(message);
    }
}
