package fr.afpa.dev.pompey.conversaapi.modele;

import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Signalements {
    private MessagesPrivee messagesPrivee;
    private User user;
    private Timestamp date;
    private String raison;

    // CONSTRUCTEURS
    /**
     * Constructeur par défaut
     */
    public Signalements() {
    }

    /**
     * Pour signaler un message
     * @param messagesPrivee
     * @param user
     */
    public Signalements(MessagesPrivee messagesPrivee, User user) {
        this.messagesPrivee = messagesPrivee;
        this.user = user;
        this.date = new Timestamp(System.currentTimeMillis());
        this.raison = "NONE";
    }

    public Signalements(MessagesPrivee messagesPrivee, User user, Timestamp date, String raison) {
        this.messagesPrivee = messagesPrivee;
        this.user = user;
        this.date = date;
        this.raison = raison;
    }

    public Signalements(MessagesPrivee messagesPrivee, String raison) {
        this.messagesPrivee = messagesPrivee;
        this.raison = raison;
    }

    /**
     * Pour trouver un signalement
     * @param messagesPrivee
     */
    public Signalements(MessagesPrivee messagesPrivee) {
        this.messagesPrivee = messagesPrivee;
    }

    // GETTERS ET SETTERS

    public MessagesPrivee getMessagesPrivee() {
        return messagesPrivee;
    }

    public void setMessagesPrivee(MessagesPrivee messagesPrivee) throws SaisieException {
        if(messagesPrivee == null) {
            throw new SaisieException("Le message ne doit pas être vide ou null");
        }
        this.messagesPrivee = messagesPrivee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) throws SaisieException {
        if(user == null) {
            throw new SaisieException("L'utilisateur ne doit pas être vide ou null");
        }
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) throws SaisieException {
        if (date == null) {
            throw new SaisieException("La date ne doit pas être vide ou null");
        } else if (date.toLocalDateTime().toLocalDate().isAfter(LocalDate.now())) {
            throw new SaisieException("La date ne corresponds pas");
        }
        this.date = date;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) throws SaisieException {
        if(raison == null || raison.isEmpty()) {
            throw new SaisieException("La raison ne doit pas être vide ou null");
        }
        this.raison = raison;
    }

    @Override
    public String toString() {
        return "Signalements{" +
                "messagesPrivee=" + messagesPrivee +
                ", user=" + user +
                ", date=" + date +
                ", raison='" + raison + '\'' +
                '}';
    }
}
