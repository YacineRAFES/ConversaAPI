package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.exception.DAOException;
import fr.afpa.dev.pompey.conversaapi.exception.RegexException;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
/**
 * Classe DAO pour les opérations CRUD sur les utilisateurs.
 */
public class UserDAO extends DAO<User>{

    public UserDAO(Connection connect) {
        this.connect = connect;
    }

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
                "VALUES (?, ?, ?, ?, ?)");
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
            log.error("Erreur lors de la création User", e);
            throw new DAOException(e.getMessage());
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
        log.info("delete: Suppression de l'utilisateur avec ID : " + obj.getId());
        StringBuilder deleteSQL = new StringBuilder("DELETE FROM utilisateur WHERE USER_ID = ?");

        try {
            PreparedStatement pstmt = connect.prepareStatement(deleteSQL.toString());
            pstmt.setInt(1, obj.getId());
            pstmt.executeUpdate();
            log.info("delete: Utilisateur supprimé avec succès "+ obj.getId());
        } catch (SQLException | DAOException e) {
            log.error("Erreur lors de la deletion User", e);
            throw new DAOException(e.getMessage());
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
        String updateSQL =
                        "UPDATE utilisateur " +
                        "SET USER_EMAIL = ?, " +
                        "USER_PASSWORD = ?, " +
                        "USER_NAME = ?, " +
                        "USER_DATE = ?, " +
                        "USER_ROLE = ? " +
                        "WHERE USER_ID = ? ";

        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL);
            pstmt.setString(1, obj.getEmail());
            pstmt.setString(2, obj.getPassword());
            pstmt.setString(3, obj.getName());
            pstmt.setDate(4, obj.getDate());
            pstmt.setString(5, obj.getRole());
            pstmt.setInt(6, obj.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | DAOException e) {
            log.error("Erreur lors de la modification User",e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Trouve un utilisateur par son ID avec un compte valide.
     *
     * @param id L'ID de l'utilisateur à trouver.
     * @return L'utilisateur trouvé, ou null si aucun utilisateur n'a été trouvé.
     */
    @Override
    public User find(int id) {
        User user = new User(id);

        String selectById = "SELECT * FROM utilisateur WHERE USER_ID = ? AND USER_ISVALID=1";

        try{
            PreparedStatement pstmt = connect.prepareStatement(selectById);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                user.setId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("USER_EMAIL"));
                user.setPassword(rs.getString("USER_PASSWORD"));
                user.setName(rs.getString("USER_NAME"));
                user.setDate(rs.getDate("USER_DATE"));
                user.setRole(rs.getString("USER_ROLE"));
            }
            return user;
        }catch (SQLException | SaisieException | RegexException e){
            log.error("Erreur lors de la recherche User",e);
            throw new DAOException(e.getMessage());
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
        String sql = "SELECT * FROM utilisateur WHERE USER_ISVALID=1";

        try(PreparedStatement ps = connect.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("USER_EMAIL"));
                user.setPassword(rs.getString("USER_PASSWORD"));
                user.setName(rs.getString("USER_NAME"));
                user.setDate(rs.getDate("USER_DATE"));
                user.setRole(rs.getString("USER_ROLE"));
                users.add(user);
            }
            return users;
        }catch (SQLException | SaisieException | RegexException e){
            log.error("Erreur lors de la recherche de tous les utilisateurs", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Trouve un utilisateur par le nom
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        log.info("findByUsername: Recherche de l'utilisateur par nom : " + username);
        User user = new User();
        String sql =
                "SELECT * " +
                "FROM utilisateur " +
                "WHERE USER_NAME = ? " +
                "AND USER_ISVALID=1";

        try(PreparedStatement ps = connect.prepareStatement(sql)){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                user.setId(rs.getInt("USER_ID"));
                user.setName(rs.getString("USER_NAME"));
                user.setEmail(rs.getString("USER_EMAIL"));
                user.setPassword(rs.getString("USER_PASSWORD"));
                user.setDate(rs.getDate("USER_DATE"));
                user.setRole(rs.getString("USER_ROLE"));
            }
            return user;
        }catch (SQLException | SaisieException | RegexException e){
            log.error("Erreur lors de la recherche de l'utilisateur par nom", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Désactive un compte d'utilisateur
     * @param obj
     * @return
     */
    public boolean disableAccount(User obj){
        log.info("Desactiver le compte : " + obj.getName());
        String disableAccountSQL =
                "UPDATE utilisateur " +
                "SET USER_ISVALID = 0 " +
                "WHERE USER_ID = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(disableAccountSQL);
            pstmt.setInt(1, obj.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | DAOException e) {
            log.error("Erreur lors de la modification User pour desactiver le compte",e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Trouve tous les utilisateurs seulement par le rôle utilisateur et modérateur dans la base de données.
     *
     * @return Une liste de tous les utilisateurs.
     */
    public List<User> findAllOnlyUsersAndModo() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT USER_ID, USER_NAME, USER_EMAIL, USER_DATE, USER_ROLE, USER_ISVALID FROM utilisateur WHERE USER_ROLE IN ('user', 'moderator')";

        try(PreparedStatement ps = connect.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("USER_EMAIL"));
                user.setName(rs.getString("USER_NAME"));
                user.setDate(rs.getDate("USER_DATE"));
                user.setRole(rs.getString("USER_ROLE"));
                user.setValide(rs.getBoolean("USER_ISVALID"));
                users.add(user);
            }
            return users;
        }catch (SQLException | SaisieException | RegexException e){
            log.error("Erreur lors de la recherche de tous les utilisateurs", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Trouve un utilisateur par son ID.
     *
     * @param obj L'ID de l'utilisateur à trouver.
     * @return L'utilisateur trouvé, ou null si aucun utilisateur n'a été trouvé.
     */
    public User findByIdUserForAdmin(User obj) {
        log.info("findByIdUserForAdmin : " + obj.getId());
        User user = new User();
        String selectById = "SELECT USER_ID, USER_EMAIL, USER_NAME, USER_DATE, USER_ROLE, USER_ISVALID FROM utilisateur WHERE USER_ID = ?";

        try{
            PreparedStatement pstmt = connect.prepareStatement(selectById);
            pstmt.setInt(1, obj.getId());
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                user.setId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("USER_EMAIL"));
                user.setName(rs.getString("USER_NAME"));
                user.setDate(rs.getDate("USER_DATE"));
                user.setRole(rs.getString("USER_ROLE"));
                user.setValide(rs.getBoolean("USER_ISVALID"));
            }
            return user;
        }catch (SQLException | SaisieException | RegexException e){
            log.error("Erreur lors de la recherche User",e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Mise à jour un compte d'utilisateur par un Admin
     * @param obj
     * @return
     */
    public boolean updateByAdmin(User obj) {
        String updateSQL =
                "UPDATE utilisateur " +
                        "SET USER_EMAIL = ?, " +
                        "USER_NAME = ?, " +
                        "USER_ROLE = ?, " +
                        "USER_ISVALID = ? " +
                        "WHERE USER_ID = ? ";

        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL);
            pstmt.setString(1, obj.getEmail());
            pstmt.setString(2, obj.getName());
            pstmt.setString(3, obj.getRole());
            pstmt.setBoolean(4, obj.isValide());
            pstmt.setInt(5, obj.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | DAOException e) {
            log.error("Erreur lors de la modification User",e);
            throw new DAOException(e.getMessage());
        }
    }

    public User findByEmail(String email){
        log.info("findByEmail: Recherche de l'utilisateur par email : " + email);
        User user = new User();
        String sql =
                "SELECT * " +
                        "FROM utilisateur " +
                        "WHERE USER_EMAIL = ? " +
                        "AND USER_ISVALID=1";

        try(PreparedStatement ps = connect.prepareStatement(sql)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                user.setId(rs.getInt("USER_ID"));
                user.setName(rs.getString("USER_NAME"));
                user.setEmail(rs.getString("USER_EMAIL"));
                user.setPassword(rs.getString("USER_PASSWORD"));
                user.setDate(rs.getDate("USER_DATE"));
                user.setRole(rs.getString("USER_ROLE"));
            }
            return user;
        }catch (SQLException | SaisieException | RegexException e){
            log.error("Erreur lors de la recherche de l'utilisateur par nom", e);
            throw new DAOException(e.getMessage());
        }
    }

}