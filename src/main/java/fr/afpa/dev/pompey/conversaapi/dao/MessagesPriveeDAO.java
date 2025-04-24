package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.exception.DAOException;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class MessagesPriveeDAO extends DAO<MessagesPrivee> {

    public MessagesPriveeDAO(Connection connect) {
        this.connect = connect;
    }
    @Override
    public int create(MessagesPrivee obj) {
        int newid = 0;
        String insertSQL =
                "INSERT INTO message_privee " +
                        "(MP_DATE, MP_MESSAGES, USER_ID, MG_ID)" +
                        "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connect.prepareStatement(insertSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, obj.getDate());
            pstmt.setString(2, obj.getMessage());
            pstmt.setInt(3, obj.getIdUser());
            pstmt.setInt(4, obj.getIdGroupeMessagesPrives());
            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    newid = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Erreur lors de la création Messages Privee", e);
            throw new DAOException(e.getMessage());
        }
        return newid;
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
        String updateSQL = "UPDATE message_privee SET MP_DATE = ?, MP_MESSAGES = ?, USER_ID = ?, MG_ID = ? WHERE MP_ID = ?";

        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL);
            pstmt.setTimestamp(1, obj.getDate());
            pstmt.setString(2, obj.getMessage());
            pstmt.setInt(3, obj.getIdUser());
            pstmt.setInt(4, obj.getIdGroupeMessagesPrives());
            pstmt.setInt(5, obj.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | DAOException e) {
            log.error("Erreur lors de la modification d'un message privee",e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public MessagesPrivee find(int id) {

        return null;
    }

    @Override
    public List<MessagesPrivee> findAll() {
        return List.of();
    }
}
