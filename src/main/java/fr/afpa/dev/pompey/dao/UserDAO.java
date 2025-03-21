package fr.afpa.dev.pompey.dao;

import fr.afpa.dev.pompey.exception.DAOException;
import fr.afpa.dev.pompey.exception.SaisieException;
import fr.afpa.dev.pompey.modele.User;
import fr.afpa.dev.pompey.utilitaires.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour les opérations CRUD sur les utilisateurs.
 */
public class UserDAO extends DAO<User>{

    /**
     * Crée un nouvel utilisateur dans la base de données.
     *
     * @param obj L'utilisateur a créé
     * @return L'ID du nouvel utilisateur créé.
     */
    @Override
    public int create(User obj) {
        int newid = 0;
        StringBuilder insertSQL = new StringBuilder(
                "INSERT INTO utilisateur " +
                "(USER_EMAIL, USER_PASSWORD, USER_NAME, USER_ROLE, USER_DATE)" +
                "+ VALUES (?, ?, ?, ?, ?)");
        try {
            PreparedStatement pstmt = connect.prepareStatement(insertSQL.toString(),
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, obj.getEmail());
            pstmt.setString(2, obj.getPassword());
            pstmt.setString(3, obj.getName());
            pstmt.setString(4, obj.getRole());
            pstmt.setDate(5, obj.getDate());
            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    newid = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Log.error("Erreur lors de la création User", e);
        }
        return newid;
    }

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param obj L'utilisateur à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    @Override
    public boolean delete(User obj) {
        StringBuilder deleteSQL = new StringBuilder("DELETE FROM utilisateur WHERE USER_ID = ?");

        try {
            PreparedStatement pstmt = connect.prepareStatement(deleteSQL.toString());
            pstmt.setInt(1, obj.getId());
            pstmt.executeUpdate();
        } catch (SQLException | DAOException e) {
            Log.error("Erreur lors de la deletion User", e);
        }
        return true;
    }

    /**
     * Met à jour les informations d'un utilisateur dans la base de données.
     *
     * @param obj L'utilisateur à mettre à jour.
     * @return true si la mise à jour a réussi, false sinon.
     */
    @Override
    public boolean update(User obj) {
        StringBuilder updateSQL = new StringBuilder("UPDATE utilisateur SET USER_EMAIL = ?, USER_PASSWORD = ?, USER_NAME = ?, USER_DATE = ?, USER_ROLE = ? WHERE USER_ID = ?");

        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL.toString());
            pstmt.setString(1, obj.getEmail());
            pstmt.setString(2, obj.getPassword());
            pstmt.setString(3, obj.getName());
            pstmt.setDate(4, obj.getDate());
            pstmt.setString(5, obj.getRole());
            pstmt.setInt(6, obj.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | DAOException e) {
            Log.error("Erreur lors de la modification User",e);
            return false;
        }
    }

    /**
     * Trouve un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à trouver.
     * @return L'utilisateur trouvé, ou null si aucun utilisateur n'a été trouvé.
     */
    @Override
    public User find(int id) {
        User user = new User(id);

        StringBuilder selectById = new StringBuilder("SELECT * FROM utilisateur WHERE USER_ID = ?");

        try{
            PreparedStatement pstmt = connect.prepareStatement(selectById.toString());
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                users(user, rs);
            }
            return user;
        }catch (SQLException | SaisieException e){
            Log.error("Erreur lors de la recherche User",e);
            return null;
        }
    }

    /**
     * Trouve tous les utilisateurs dans la base de données.
     *
     * @return Une liste de tous les utilisateurs.
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        StringBuilder selectSQL = new StringBuilder("SELECT * FROM utilisateur");

        try{
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL.toString());

            while(rs.next()){
                User user = new User();
                users(user, rs);
                users.add(user);
            }

        }catch (SQLException | SaisieException e){
            throw new RuntimeException(e);
        }

        return users;
    }

    private void users(User user, ResultSet rs) throws SQLException, SaisieException {
        user.setId(rs.getInt("USER_ID"));
        user.setEmail(rs.getString("USER_EMAIL"));
        user.setPassword(rs.getString("USER_PASSWORD"));
        user.setName(rs.getString("USER_NAME"));
        user.setDate(rs.getDate("USER_DATE"));
        user.setRole(rs.getString("USER_ROLE"));
    }

}
