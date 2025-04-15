package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;

import java.sql.Connection;

public class DAOFactory {
    private static DAOFactory instance;

    private final Connection connection;

    // Constructeur privé qui reçoit la connexion
    private DAOFactory(Role role) {
        this.connection = Singleton.getInstanceDB(role.name().toLowerCase());
    }

    // Méthode getInstance avec paramètre "role"
    public static DAOFactory getInstance(Role role) {
        if (instance == null) {
            instance = new DAOFactory(role);
        }
        return instance;
    }

    public UserDAO getUserDAO() {
        return new UserDAO(connection);
    }

    public AmisDAO getAmisDAO() {
        return new AmisDAO(connection);
    }

    // Ajout d'autres DAO ici si nécessaire
}
