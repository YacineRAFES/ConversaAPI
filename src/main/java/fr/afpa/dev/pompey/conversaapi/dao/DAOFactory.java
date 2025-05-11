package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DAOFactory {
    private static DAOFactory instance;

    private final Connection connection;

    // Constructeur privé qui reçoit la connexion
    public DAOFactory(Role role) {
        log.info("Role : " + role);
        this.connection = Singleton.getInstanceDB(role.name().toLowerCase());
    }

    private static final Map<Role, DAOFactory> instances = new HashMap<>();

    public static synchronized DAOFactory getInstance(Role role) {
        return instances.computeIfAbsent(role, DAOFactory::new);
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
