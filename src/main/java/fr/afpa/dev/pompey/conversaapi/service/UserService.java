package fr.afpa.dev.pompey.conversaapi.service;

import fr.afpa.dev.pompey.conversaapi.dao.DAOFactory;
import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;

import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService(Role role){
        DAOFactory daoFactory = new DAOFactory(role);
        this.userDAO = daoFactory.getUserDAO();
    }

    /**
     * Récupère tous les utilisateurs de la base de données.
     *
     * @return La liste de tous les utilisateurs.
     */
    public List<User> getAll() {
        return userDAO.findAll();
    }

    /**
     * Ajoute un utilisateur à la base de données.
     *
     * @param user L'utilisateur à ajouter.
     * @return
     */
    public int add(User user) throws IllegalStateException  {
        try{
            return userDAO.create(user);
        }catch (Exception e){
            throw new IllegalStateException ("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage() + Utils.getNameClass());
        }

    }

    /**
     * Met à jour un utilisateur dans la base de données.
     *
     * @param user L'utilisateur à mettre à jour.
     * @return
     */
    public boolean update(User user) {
        return userDAO.update(user);
    }

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param user L'utilisateur à supprimer.
     * @return
     */
    public boolean delete(User user) {
        return userDAO.delete(user);
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à récupérer.
     * @return L'utilisateur correspondant à l'ID.
     */
    public User get(int id) {
        return userDAO.find(id);
    }

    public User findByName(String name) {
        return userDAO.findByUsername(name);
    }
}
