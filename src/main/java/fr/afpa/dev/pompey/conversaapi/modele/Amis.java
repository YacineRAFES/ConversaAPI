package fr.afpa.dev.pompey.conversaapi.modele;

import java.sql.Date;
import java.time.LocalDate;

public class Amis {
    private int statut;
    private Date dateDemande;
    private int userIdDemandeur;
    private int userIdAmiDe;

    public Amis(){}

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public int getUserIdDemandeur() {
        return userIdDemandeur;
    }

    public void setUserIdDemandeur(int userIdDemandeur) {
        this.userIdDemandeur = userIdDemandeur;
    }

    public int getUserIdAmiDe() {
        return userIdAmiDe;
    }

    public void setUserIdAmiDe(int userIdAmiDe) {
        this.userIdAmiDe = userIdAmiDe;
    }
}
