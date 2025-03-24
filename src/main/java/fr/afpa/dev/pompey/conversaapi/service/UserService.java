package fr.afpa.dev.pompey.conversaapi.service;

import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.modele.User;

import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService(){
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public void addUser(User user) {
        userDAO.create(user);
    }
}
