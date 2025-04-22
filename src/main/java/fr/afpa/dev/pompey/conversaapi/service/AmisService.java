package fr.afpa.dev.pompey.conversaapi.service;

import fr.afpa.dev.pompey.conversaapi.dao.AmisDAO;
import fr.afpa.dev.pompey.conversaapi.dao.DAOFactory;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;

import java.util.List;

public class AmisService {

    private final AmisDAO amisDAO;

    public AmisService(Role role){
        DAOFactory daoFactory = DAOFactory.getInstance(role);
        this.amisDAO = daoFactory.getAmisDAO();
    }

    /**
     * Récupère tous les utilisateurs de la base de données.
     *
     * @return La liste de tous les utilisateurs.
     */
    public List<Amis> getAll() {
        return amisDAO.findAll();
    }

    /**
     * Ajoute un utilisateur à la base de données.
     *
     * @param amis L'utilisateur à ajouter.
     * @return
     */
    public boolean add(Amis amis) throws IllegalStateException  {
        try{
            return amisDAO.createDemandeAmis(amis);
        }catch (Exception e){
            throw new IllegalStateException ("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage() + Utils.getNameClass());
        }

    }

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param amis L'utilisateur à supprimer.
     * @return
     */
    public boolean delete(Amis amis) {
        return amisDAO.delete(amis);
    }

    /**
     * Met à jour un utilisateur dans la base de données.
     *
     * @param amis L'utilisateur à mettre à jour.
     * @return
     */
    public boolean update(Amis amis) {
        return amisDAO.update(amis);
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à récupérer.
     * @return L'utilisateur correspondant à l'ID.
     */
    public Amis get(int id) {
        return amisDAO.find(id);
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à récupérer.
     * @return Retourne la liste d'amis correspondants à l'ID.
     */
    public List<Amis> findById(int id) {
        return amisDAO.findById(id);
    }

    public Amis find(int id1, int id2) {
        return amisDAO.findByUsers(id1, id2);
    }

    /**
     * Récupère tous les amis d'un utilisateur.
     *
     * @param id L'ID de l'utilisateur.
     * @return Retourne tout les demandes d'amis de l'utilisateur.
     */
    public List<Amis> findAllFriendsRequestById(int id) {
        return amisDAO.findAllFriendsRequestById(id);
    }

    /**
     * Récupère tous les amis d'un utilisateur.
     *
     * @param id L'ID de l'utilisateur.
     * @param nom Le nom de l'utilisateur à rechercher.
     * @return Retourne tout les demandes d'amis de l'utilisateur.
     */
    public List<Amis> TrouverUnAmis(String nom, int id) {
        return amisDAO.TrouverUnAmis(nom, id);
    }


}
