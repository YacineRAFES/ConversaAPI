package fr.afpa.dev.pompey.conversaapi.modele;

import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

public class MessagesPrivee {
    private Integer id;
    private Timestamp date;
    private String message;
    private User user;
    private Integer idGroupeMessagesPrives;

    // CONSTRUCTEURS
    public MessagesPrivee(){

    }

    /**
     * On l'utilise find
     * @param id
     * @param date
     * @param message
     * @param user
     * @param idGroupeMessagesPrives
     */
    public MessagesPrivee(Integer id, Timestamp date, String message, User user, Integer idGroupeMessagesPrives) {
        this.id = id;
        this.date = date;
        this.message = message;
        this.user = user;
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
    }

    /**
     * Quand un utilisateur envoie un message
     * @param message
     * @param user
     * @param idGroupeMessagesPrives
     */
    public MessagesPrivee(String message, User user, Integer idGroupeMessagesPrives) {
        this.date = new Timestamp(System.currentTimeMillis());
        this.message = message;
        this.user = user;
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
    }

    /**
     * Pour modifier un message
     * @param message
     * @param id
     */
    public MessagesPrivee(String message, Integer id) {
        this.message = message;
        this.id = id;
    }

    // GETTERS ET SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) throws SaisieException {
        if (id == null) {
            throw new SaisieException("L'id ne doit pas être vide ou null");
        } else if (id <= 0) {
            throw new SaisieException("L'id ne doit pas être négatif");
        }
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) throws SaisieException {
        if(message == null || message.isEmpty()) {
            throw new SaisieException("Le message ne doit pas être vide ou null");
        }
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) throws SaisieException {
        if (user == null) {
            throw new SaisieException("L'User ne doit pas être vide ou null");
        }
        this.user = user;
    }

    public Integer getIdGroupeMessagesPrives() {
        return idGroupeMessagesPrives;
    }

    public void setIdGroupeMessagesPrives(Integer idGroupeMessagesPrives) throws SaisieException {
        if (idGroupeMessagesPrives == null) {
            throw new SaisieException("L'idGroupeMessagesPrives ne doit pas être vide ou null");
        } else if (idGroupeMessagesPrives <= 0) {
            throw new SaisieException("L'idGroupeMessagesPrives ne doit pas être négatif");
        }
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
    }

    @Override
    public String toString() {
        return "MessagesPrivee{" +
                "id=" + id +
                ", date=" + date +
                ", message='" + message + '\'' +
                ", user=" + user.getId() +
                ", idGroupeMessagesPrives=" + idGroupeMessagesPrives.toString() +
                '}';
    }
}
