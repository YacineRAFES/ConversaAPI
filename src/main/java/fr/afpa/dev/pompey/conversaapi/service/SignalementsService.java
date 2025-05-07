package fr.afpa.dev.pompey.conversaapi.service;

import fr.afpa.dev.pompey.conversaapi.dao.DAOFactory;
import fr.afpa.dev.pompey.conversaapi.dao.SignalementsDAO;
import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Signalements;
import fr.afpa.dev.pompey.conversaapi.modele.User;

import java.util.List;

public class SignalementsService {

    private final SignalementsDAO signalementsDAO;

    public SignalementsService(Role role){
        DAOFactory daoFactory = new DAOFactory(role);
        this.signalementsDAO = daoFactory.getSignalementsDAO();
    }

    /**
     * Ajoute un signalement.
     *
     * @param signalements Le signalement à ajouter.
     * @return true si l'ajout a réussi, false sinon.
     */
    public boolean add(Signalements signalements){
        return signalementsDAO.creerUnSignalement(signalements);
    }

    /**
     * Supprime un signalement.
     *
     * @param signalements Le signalement à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean delete(Signalements signalements){
        return signalementsDAO.supprimerUnSignalement(signalements);
    }

    /**
     * Met à jour un signalement.
     *
     * @param signalements Le signalement à mettre à jour.
     * @return true si la mise à jour a réussi, false sinon.
     */
    public boolean update(Signalements signalements){
        return signalementsDAO.updateUnSignalement(signalements);
    }

    /**
     * Récupère un signalement par son IDMP.
     *
     * @param id L'ID du signalement à récupérer.
     * @return Le signalement correspondant à l'ID.
     */
    public Signalements getByIdMP(int id){
        return signalementsDAO.find(id);
    }

    /**
     * Récupère tous les signalements de la base de données.
     *
     * @return La liste de tous les signalements.
     */
    public List<Signalements> getAll() {
        return signalementsDAO.findAll();
    }

}
