package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

@Slf4j
public class DAOFactory {
    private static DAOFactory instance;

    private final Connection connection;

    // Constructeur privé qui reçoit la connexion
    private DAOFactory(Role role) {
        log.info(role.toString());
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

    public MessagesPriveeDAO getMessagesPriveeDAO() {
        return new MessagesPriveeDAO(connection);
    }

    public SignalementsDAO getSignalementsDAO() {
        return new SignalementsDAO(connection);
    }

    // Ajout d'autres DAO ici si nécessaire
}
