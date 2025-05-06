package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.exception.DAOException;
import fr.afpa.dev.pompey.conversaapi.exception.RegexException;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
import fr.afpa.dev.pompey.conversaapi.modele.Signalements;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SignalementsDAO extends DAO<Signalements> {

    public SignalementsDAO(Connection connect) {
        this.connect = connect;
    }

    @Override
    public int create(Signalements obj) {
        return 0;
    }

    @Override
    public boolean delete(Signalements obj) {
        return false;
    }

    @Override
    public boolean update(Signalements obj) {
        return false;
    }

    @Override
    public Signalements find(int id) {
        log.info("find: Recherche du signalement avec ID: " + id);
        String findSQL =
                "SELECT * FROM SIGNALEMENT sgl " +
                "JOIN MESSAGE_PRIVEE mp ON sgl.MP_ID_SIGNALER = mp.MP_ID " +
                "JOIN UTILISATEUR u1 ON sgl.USER_ID_SIGNALE = u1.USER_ID " +
                "JOIN UTILISATEUR u2 ON mp.USER_ID = u2.USER_ID " +
                "WHERE MP_ID_SIGNALER = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(findSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User utilisateurSignale = new User(
                        rs.getInt("u1.USER_ID"),
                        rs.getString("u1.USER_NAME")
                );

                User emetteurMessage = new User(
                        rs.getInt("u2.USER_ID"),
                        rs.getString("u2.USER_NAME")
                );

                MessagesPrivee messagesPrivee = new MessagesPrivee(
                        rs.getInt("MP_ID"),
                        rs.getTimestamp("MP_DATE"),
                        rs.getString("MP_MESSAGES"),
                        emetteurMessage,
                        rs.getInt("MG_ID")
                );

                return new Signalements(
                        messagesPrivee,
                        utilisateurSignale,
                        rs.getTimestamp("SGL_DATE"),
                        rs.getString("SGL_RAISON")
                );
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage() + " " + id + "Erreur lors de la récupération du signalement");
        }
        return null;
    }

    @Override
    public List<Signalements> findAll() {
        log.info("findAll: Récupération de tous les signalements");

        List<Signalements> signalements = new ArrayList<>();
        String selectAllSQL =
                "SELECT * FROM SIGNALEMENT sgl " +
                "JOIN MESSAGE_PRIVEE mp ON sgl.MP_ID_SIGNALER = mp.MP_ID " +
                "JOIN UTILISATEUR u1 ON sgl.USER_ID_SIGNALE = u1.USER_ID " +
                "JOIN UTILISATEUR u2 ON mp.USER_ID = u2.USER_ID";

        try(PreparedStatement ps = connect.prepareStatement(selectAllSQL)){

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User utilisateurSignale = new User(
                        rs.getInt("u1.USER_ID"),
                        rs.getString("u1.USER_NAME")
                );

                User emetteurMessage = new User(
                        rs.getInt("u2.USER_ID"),
                        rs.getString("u2.USER_NAME")
                );

                MessagesPrivee messagesPrivee = new MessagesPrivee(
                        rs.getInt("MP_ID"),
                        rs.getTimestamp("MP_DATE"),
                        rs.getString("MP_MESSAGES"),
                        emetteurMessage,
                        rs.getInt("MG_ID")
                );

                Signalements signalement = new Signalements(
                        messagesPrivee,
                        utilisateurSignale,
                        rs.getTimestamp("SGL_DATE"),
                        rs.getString("SGL_RAISON")
                );
                signalements.add(signalement);
            }

        }catch (SQLException e){
            log.error("Erreur lors de la recherche de tous les utilisateurs", e);
            throw new DAOException(e.getMessage());
        }
        return signalements;
    }

    public boolean creerUnSignalement(Signalements obj) {
        log.info("create: Création d'un signalement pour l'utilisateur avec ID: " + obj.getMessagesPrivee().getId());
        String insertSQL =
                "INSERT INTO SIGNALEMENT (MP_ID_SIGNALER, SGL_DATE, SGL_RAISON, USER_ID_SIGNALE)\n" +
                        "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connect.prepareStatement(insertSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, obj.getMessagesPrivee().getId());
            pstmt.setTimestamp(2, obj.getDate());
            pstmt.setString(3, obj.getRaison());
            pstmt.setInt(4, obj.getUser().getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DAOException(e.getMessage() + " " + obj.getMessagesPrivee().getId() + "Erreur lors de la création d'un signalement");
        }
    }

    public boolean supprimerUnSignalement(Signalements obj) {
        log.info("delete: Suppression du signalement avec ID: " + obj.getMessagesPrivee().getId());
        String deleteSQL = "DELETE FROM SIGNALEMENT WHERE MP_ID_SIGNALER = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(deleteSQL);
            pstmt.setInt(1, obj.getMessagesPrivee().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage() + " " + obj.getMessagesPrivee().getId() + "Erreur lors de la suppression d'un signalement");
        }
        return true;
    }

    public boolean updateUnSignalement(Signalements obj) {
        log.info("update: Modification du signalement avec ID: " + obj.getMessagesPrivee().getId() + " et raison: " + obj.getRaison());
        String updateSQL = "UPDATE SIGNALEMENT SET SGL_RAISON = ? WHERE MP_ID_SIGNALER = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL);
            pstmt.setString(1, obj.getRaison());
            pstmt.setInt(2, obj.getMessagesPrivee().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage() + " " + obj.getMessagesPrivee().getId() + "Erreur lors de la modification d'un signalement");
        }
        return true;
    }


}
