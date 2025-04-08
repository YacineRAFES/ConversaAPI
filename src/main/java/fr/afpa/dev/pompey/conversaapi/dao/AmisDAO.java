package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.exception.DAOException;
import fr.afpa.dev.pompey.conversaapi.exception.RegexException;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AmisDAO extends DAO<Amis> {

    /**
     * @param obj 
     * @return
     */
    @Override
    public int create(Amis obj) {
        int newid = 0;
        String insertSQL =
                "INSERT INTO amis " +
                "(AMIS_STATUT, AMIS_DATE_DEMANDE, USER_ID_amiDe, USER_ID_utilisateur)" +
                "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connect.prepareStatement(insertSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, obj.getStatut().name());
            pstmt.setDate(2, obj.getDateDemande());
            pstmt.setInt(3, obj.getUserIdAmiDe());
            pstmt.setInt(4, obj.getUserIdDemandeur());
            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    newid = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Erreur lors de la création Amis", e);
            throw new DAOException(e.getMessage());
        }
        return newid;
    }

    /**
     * @param obj 
     * @return
     */
    @Override
    public boolean delete(Amis obj) {
        try{
            refuserDemandeAmisOuRetireEnAmis(obj);
            return true;
        }catch(Exception e){
            log.error("Erreur lors de la suppression Amis", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Modifie une ligne d'amis dans la base de données et change le statut de la demande d'amis.
     * Et créer un groupe de messages privés.
     * @param obj 
     * @return
     */
    @Override
    public boolean update(Amis obj) {
        try{
            UtilisateurAccepteLaDemande(obj);
            return true;
        }catch (Exception e) {
            log.error("Erreur lors de la modification Amis", e);
            throw new DAOException(e.getMessage());
        }
    }
//TODO: A REVOIR IL MANQUE LE TEST UNITAIRE POUR LA DAO AMIS
    /**
     * @param id
     * @return
     */
    @Override
    public Amis find(int id) {
        log.info("Fonction find(id) appellée");
        List<User> users = new ArrayList<>();
        String selectSQL =
                "SELECT u.USER_NAME " +
                "FROM amis a " +
                "JOIN utilisateur u ON a.USER_ID_amiDe = u.USER_ID " +
                "WHERE a.USER_ID_utilisateur = ?"
                ;
        try {
            PreparedStatement pstmt = connect.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setName(rs.getString("USER_NAME"));
                users.add(user);
                log.info("Liste des amis fait");
            }
        } catch (SQLException | RegexException | SaisieException e) {
            log.error("Erreur lors de la récupération de la liste d'amis", e);
            throw new DAOException(e.getMessage());
        }
        return (Amis) users;
    }

    /**
     * @return
     */
    @Override
    public List<Amis> findAll() {
        return List.of();
    }

    /**
     * Dès, l'utilisateur refuse une demande d'amis, il supprime la ligne de la table amis.
     * Supprime un enregistrement d'amis de la base de données.
     * "DELETE FROM amis WHERE USER_ID_amiDe = ? AND USER_ID_utilisateur = ?"
     *
     * @param obj
     */
    public void refuserDemandeAmisOuRetireEnAmis(Amis obj) {
        String deleteSQL = "DELETE FROM amis WHERE USER_ID_amiDe = ? AND USER_ID_utilisateur = ? OR USER_ID_amiDe = ? AND USER_ID_utilisateur = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(deleteSQL);
            pstmt.setInt(1, obj.getUserIdAmiDe());
            pstmt.setInt(2, obj.getUserIdDemandeur());
            pstmt.setInt(3, obj.getUserIdDemandeur());
            pstmt.setInt(4, obj.getUserIdAmiDe());
            pstmt.executeUpdate();
            log.info("Demande d'amis refusée {} et {}", obj.getUserIdDemandeur(), obj.getUserIdAmiDe());
        } catch (SQLException e) {
            log.error("Erreur lors de la deletion Amis", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Méthode pour accepter une demande d'amis
     * @param obj
     */
    public void UtilisateurAccepteLaDemande(Amis obj) {
        try{
            int newIdMessageGroupe = createGroupeMessagePrivee();
            accepterDemandeAmis(obj.getUserIdDemandeur(), obj.getUserIdAmiDe(), newIdMessageGroupe);
            dubliquerAmis(obj.getUserIdDemandeur(), obj.getUserIdAmiDe(), newIdMessageGroupe);
        }catch (DAOException e){
            log.error("Erreur lors de la modification Amis", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Méthode pour accepter une demande d'amis
     * @param idDemandeur
     * @param idDemandeAccepter
     * @param idGroupeMessagesPrives
     */
    public void accepterDemandeAmis(int idDemandeur, int idDemandeAccepter, int idGroupeMessagesPrives) {
        String updateSQL =
                "UPDATE amis " +
                "SET AMIS_STATUT = ?, MG_ID = ? " +
                "WHERE USER_ID_utilisateur = ? " +
                "AND USER_ID_amiDe = ?";
        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL);
            pstmt.setString(1, "AMI");
            pstmt.setInt(2, idGroupeMessagesPrives);
            pstmt.setInt(3, idDemandeur);
            pstmt.setInt(4, idDemandeAccepter);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Erreur lors de l'acceptation de la demande d'amis", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Méthode pour dupliquer une ligne d'amis afin de faciliter la recherche
     * @param idDemandeur
     * @param idDemandeAccepter
     * @param idGroupeMessagesPrives
     */
    public void dubliquerAmis(int idDemandeur, int idDemandeAccepter, int idGroupeMessagesPrives) {
        String insertSQL =
                "INSERT INTO amis " +
                "(MG_ID, AMIS_STATUT, AMIS_DATE_DEMANDE, USER_ID_utilisateur, USER_ID_amiDe)" +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connect.prepareStatement(insertSQL);
            pstmt.setInt(1, idGroupeMessagesPrives);
            pstmt.setString(2, "AMI");
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));
            pstmt.setInt(4, idDemandeAccepter);
            pstmt.setInt(5, idDemandeur);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Erreur lors de la duplication d'une ligne d'amis", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Méthode pour créer un groupe de messages privés
     * @return id du groupe de messages privés
     */
    public int createGroupeMessagePrivee(){
        int newid = 0;
        String createSQL =
                "INSERT INTO groupe_messages_prives () VALUES ()";
        try {
            PreparedStatement pstmt = connect.prepareStatement(createSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.executeUpdate();
            try(ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    newid = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Erreur lors de la création de groupe_messages_prives", e);
            throw new DAOException(e.getMessage());
        }
        return newid;
    }
}
