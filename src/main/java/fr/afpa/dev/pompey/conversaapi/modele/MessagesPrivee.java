package fr.afpa.dev.pompey.conversaapi.modele;

import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

public class MessagesPrivee {
    private Integer id;
    private Timestamp date;
    private String message;
    private Integer idUser;
    private Integer idGroupeMessagesPrives;

    // CONSTRUCTEURS
    public MessagesPrivee(){

    }

    /**
     * On l'utilise find
     * @param id
     * @param date
     * @param message
     * @param idUser
     * @param idGroupeMessagesPrives
     */
    public MessagesPrivee(Integer id, Timestamp date, String message, Integer idUser, Integer idGroupeMessagesPrives) {
        this.id = id;
        this.date = date;
        this.message = message;
        this.idUser = idUser;
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
    }

    /**
     * Quand un utilisateur envoie un message
     * @param message
     * @param idUser
     * @param idGroupeMessagesPrives
     */
    public MessagesPrivee(String message, Integer idUser, Integer idGroupeMessagesPrives) {
        this.date = new Timestamp(System.currentTimeMillis());
        this.message = message;
        this.idUser = idUser;
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
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

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) throws SaisieException {
        if (idUser == null) {
            throw new SaisieException("L'idUser ne doit pas être vide ou null");
        } else if (idUser <= 0) {
            throw new SaisieException("L'idUser ne doit pas être négatif");
        }
        this.id = id;
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
}
