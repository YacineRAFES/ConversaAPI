package fr.afpa.dev.pompey.conversaapi.service;

import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.modele.User;

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
        userDAO.create(user);
    }
}
