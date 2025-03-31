package fr.afpa.dev.pompey.conversaapi.service;

import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;

import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    /**
     * Récupère tous les utilisateurs de la base de données.
     *
     * @return La liste de tous les utilisateurs.
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Ajoute un utilisateur à la base de données.
     *
     * @param user L'utilisateur à ajouter.
     */
    public void addUser(User user) {
        try{
            userDAO.create(user);
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage() + Utils.getNameClass());
        }

    }

    /**
     * Met à jour un utilisateur dans la base de données.
     *
     * @param user L'utilisateur à mettre à jour.
     */
    public void updateUser(User user) {
        userDAO.update(user);
    }

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param user L'utilisateur à supprimer.
     */
    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à récupérer.
     * @return L'utilisateur correspondant à l'ID.
     */
    public User getUser(int id) {
        return userDAO.find(id);
    }
}
