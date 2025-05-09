package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.exception.DAOException;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessagesPriveeDAO extends DAO<MessagesPrivee> {

    public MessagesPriveeDAO(Connection connect) {
        this.connect = connect;
    }
    @Override
    public int create(MessagesPrivee obj) {
        return 0;
    }

    public int createMessagesPrives(MessagesPrivee obj){
        int newId = 0;
        String insertSQL =
                "INSERT INTO message_privee " +
                        "(MP_DATE, MP_MESSAGES, USER_ID, MG_ID)" +
                        "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connect.prepareStatement(insertSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, obj.getDate());
            pstmt.setString(2, obj.getMessage());
            pstmt.setInt(3, obj.getUser().getId());
            pstmt.setInt(4, obj.getIdGroupeMessagesPrives());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
            }
            return newId;
        } catch (SQLException e) {
            log.error("Erreur lors de la création d'un message privé", e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public boolean delete(MessagesPrivee obj) {
        String deleteSQL = "DELETE FROM message_privee WHERE MP_ID = ?";

        try {
            PreparedStatement pstmt = connect.prepareStatement(deleteSQL);
            pstmt.setInt(1, obj.getId());
            pstmt.executeUpdate();
        } catch (SQLException | DAOException e) {
            log.error("Erreur lors de la deletion d'un message privee", e);
            throw new DAOException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean update(MessagesPrivee obj) {
        if (obj.getId() == null) {
            throw new DAOException("Impossible de mettre à jour un message privé sans ID !");
        }
        String updateSQL = "UPDATE message_privee SET MP_MESSAGES = ? WHERE MP_ID = ?";

        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL);
            pstmt.setString(1, obj.getMessage());
            pstmt.setInt(2, obj.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | DAOException e) {
            log.error("Erreur lors de la modification d'un message privee",e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public MessagesPrivee find(int id) {
        String selectSQL = "SELECT * FROM message_privee mp JOIN utilisateur u ON u.USER_ID = mp.USER_ID WHERE mp.MP_ID = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_NAME")
                );
                MessagesPrivee messagesPrivee = new MessagesPrivee(
                        rs.getInt("MP_ID"),
                        rs.getTimestamp("MP_DATE"),
                        rs.getString("MP_MESSAGES"),
                        user,
                        rs.getInt("MG_ID")
                );
                return messagesPrivee;
            }
        } catch (SQLException e) {
            log.error("Erreur lors de la recherche d'un message privee", e);
            throw new DAOException(e.getMessage());
        }

        return null;
    }

    @Override
    public List<MessagesPrivee> findAll() {
        List<MessagesPrivee> messagesPrivees = new ArrayList<>();
        String selectAll = "SELECT * FROM message_privee mp JOIN utilisateur u on u.USER_ID = mp.USER_ID";
        try{
            PreparedStatement pstmt = connect.prepareStatement(selectAll);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_NAME")
                );
                MessagesPrivee mp = new MessagesPrivee();
                mp.setId(rs.getInt("MP_ID"));
                mp.setDate(rs.getTimestamp("MP_DATE"));
                mp.setMessage(rs.getString("MP_MESSAGES"));
                mp.setUser(user);
                mp.setIdGroupeMessagesPrives(rs.getInt("MG_ID"));
                messagesPrivees.add(mp);
            }
            return messagesPrivees;
        }catch (SQLException | DAOException e) {
            throw new DAOException(e.getMessage());
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MessagesPrivee> findAllMessagesPriveesByIdUser(int idUser) {
        List<MessagesPrivee> messagesPrivees = new ArrayList<>();
        String selectAll =
                "SELECT DISTINCT mp.MP_ID, mp.MP_DATE, mp.MP_MESSAGES, mp.USER_ID, mp.MG_ID, u.USER_NAME " +
                "FROM message_privee mp " +
                "INNER JOIN utilisateur u ON mp.USER_ID = u.USER_ID " +
                "INNER JOIN groupe_messages_prives gmp ON mp.MG_ID = gmp.MG_ID " +
                "INNER JOIN amis a ON gmp.MG_ID = a.MG_ID " +
                "WHERE a.USER_ID_utilisateur = ? " +
                "AND mp.MP_ISDELETE = 0";
        try{
            PreparedStatement pstmt = connect.prepareStatement(selectAll);
            pstmt.setInt(1, idUser);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //TODO: AVATAR DE L'UTILISATEUR
                User user = new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_NAME"),
                        rs.getDate("USER_DATE")
                );

                MessagesPrivee mp = new MessagesPrivee(
                        rs.getInt("MP_ID"),
                        rs.getTimestamp("MP_DATE"),
                        rs.getString("MP_MESSAGES"),
                        user,
                        rs.getInt("MG_ID")
                );
                messagesPrivees.add(mp);
            }
            return messagesPrivees;
        }catch (SQLException | DAOException e) {
            throw new DAOException(e.getMessage());
        }
    }

//    public MessagesPrivee signalerUnMessage(MessagesPrivee obj) {
//        String updateSQL = "UPDATE message_privee SET MP_SIGNALER = 1 WHERE MP_ID = ?";
//        try {
//            PreparedStatement pstmt = connect.prepareStatement(updateSQL);
//            pstmt.setInt(1, obj.getId());
//            pstmt.executeUpdate();
//            return obj;
//        } catch (SQLException e) {
//            log.error("Erreur lors de la signalisation d'un message privee", e);
//            throw new DAOException(e.getMessage());
//        }
//    }

    public MessagesPrivee supprimerUnMessage(MessagesPrivee obj) {
        String updateSQL = "UPDATE message_privee SET MP_ISDELETE = 1 WHERE MP_ID = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL);
            pstmt.setInt(1, obj.getId());
            pstmt.executeUpdate();
            return obj;
        } catch (SQLException e) {
            log.error("Erreur lors de la suppression d'un message privee", e);
            throw new DAOException(e.getMessage());
        }
    }
}
