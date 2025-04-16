package fr.afpa.dev.pompey.conversaapi.modele;

import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Amis {
    private User user;
    private List<Amis> amisList;
    private Integer idGroupeMessagesPrives;
    private StatutAmitie statut;
    private Date dateDemande;
    private Integer userIdDemandeur;
    private Integer userIdAmiDe;

    // CONSTRUCTEURS

    public Amis() {
    }

    public Amis(List<Amis> amisList){
        this.amisList = amisList;
    }

    /**
     * Quand un utilisateur envoye une demande d'ami
     * @return
     */
    public Amis(Integer userIdDemandeur, Integer userIdAmiDe) {
        this.userIdDemandeur = userIdDemandeur;
        this.userIdAmiDe = userIdAmiDe;
        this.dateDemande = Date.valueOf(LocalDate.now());
        this.statut = StatutAmitie.EN_ATTENTE;
    }

    public Amis(Integer idGroupeMessagesPrives, Integer userIdDemandeur, Integer userIdAmiDe, Date dateDemande, StatutAmitie statut) {
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
        this.userIdDemandeur = userIdDemandeur;
        this.userIdAmiDe = userIdAmiDe;
        this.dateDemande = dateDemande;
        this.statut = statut;
    }

    public Amis(Integer idGroupeMessagesPrives, Integer userIdDemandeur, Integer userIdAmiDe) {
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
        this.userIdDemandeur = userIdDemandeur;
        this.userIdAmiDe = userIdAmiDe;
        this.statut = StatutAmitie.AMI;
    }

    /**
     * On l'utilise find
     * @return
     */
    public Amis(Integer idGroupeMessagesPrives, Integer userIdDemandeur, Integer userIdAmiDe, StatutAmitie statut) {
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
        this.userIdDemandeur = userIdDemandeur;
        this.userIdAmiDe = userIdAmiDe;
        this.statut = statut;
    }

    public StatutAmitie getStatut() {
        return statut;
    }

    public Integer getIdGroupeMessagesPrives() {
        return idGroupeMessagesPrives;
    }

    public void setIdGroupeMessagesPrives(Integer idGroupeMessagesPrives) throws SaisieException {
        this.idGroupeMessagesPrives = idGroupeMessagesPrives;
    }

    public void setStatut(StatutAmitie statut) throws SaisieException {
        if(statut == null) {
            throw new SaisieException("Le statut ne doit pas être vide ou null");
        }
        this.statut = statut;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) throws SaisieException {
        if (dateDemande == null) {
            throw new SaisieException("La date ne doit pas être vide ou null");
        } else if (dateDemande.toLocalDate().isAfter(LocalDate.now())) {
            throw new SaisieException("La date ne corresponds pas");
        }
        this.dateDemande = dateDemande;
    }

    public int getUserIdDemandeur() {
        return userIdDemandeur;
    }

    public void setUserIdDemandeur(Integer userIdDemandeur) throws SaisieException {
        if (userIdDemandeur == null) {
            throw new SaisieException("L'id ne doit pas être vide ou null");
        } else if (userIdDemandeur <= 0) {
            throw new SaisieException("L'id ne doit pas être négatif");
        }
        this.userIdDemandeur = userIdDemandeur;
    }

    public int getUserIdAmiDe() {
        return userIdAmiDe;
    }

    public void setUserIdAmiDe(Integer userIdAmiDe) throws SaisieException {
        if (userIdAmiDe == null) {
            throw new SaisieException("L'id ne doit pas être vide ou null");
        } else if (userIdAmiDe <= 0) {
            throw new SaisieException("L'id ne doit pas être négatif");
        }
        this.userIdAmiDe = userIdAmiDe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
