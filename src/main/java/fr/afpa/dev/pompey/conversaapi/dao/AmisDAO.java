package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.exception.DAOException;
import fr.afpa.dev.pompey.conversaapi.exception.RegexException;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.StatutAmitie;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AmisDAO extends DAO<Amis> {

    public AmisDAO(Connection connect) {
        this.connect = connect;
    }
    /**
     * @param obj
     * @return
     */
    @Override
    public int create(Amis obj) {
        throw new UnsupportedOperationException("Use createDemandeAmis instead");
    }

    public boolean createDemandeAmis(Amis obj) {
        int newId_MG = createGroupeMessagePrivee();
        String insertSQL =
                "INSERT INTO amis " +
                "(AMIS_STATUT, AMIS_DATE_DEMANDE, USER_ID_utilisateur, USER_ID_amiDe, MG_ID)" +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connect.prepareStatement(insertSQL);
            pstmt.setString(1, obj.getStatut().name());
            pstmt.setDate(2, obj.getDateDemande());
            pstmt.setInt(3, obj.getUserIdDemandeur());
            pstmt.setInt(4, obj.getUserIdAmiDe());
            pstmt.setInt(5, newId_MG);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error("Erreur lors de la création d'une demande d'amis", e);
            throw new DAOException(e.getMessage());
        }
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

    /**
     * @param id
     * @return
     */
    @Override
    public Amis find(int id) {
        log.info("Fonction find(id) appellée");
        List<Amis> amis = new ArrayList<>();
        String selectSQL =
                "SELECT * " +
                "FROM amis a " +
                "JOIN utilisateur u ON a.USER_ID_amiDe = u.USER_ID " +
                "WHERE a.USER_ID_utilisateur = ?"
                ;
        try {
            PreparedStatement pstmt = connect.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Amis ami = new Amis();
                ami.setIdGroupeMessagesPrives(rs.getInt("MG_ID"));
                ami.setStatut(StatutAmitie.valueOf(rs.getString("AMIS_STATUT")));
                ami.setDateDemande(rs.getDate("AMIS_DATE_DEMANDE"));
                ami.setUserIdAmiDe(rs.getInt("USER_ID_amiDe"));
                ami.setUserIdDemandeur(rs.getInt("USER_ID_utilisateur"));
                amis.add(ami);
                log.info("Liste des amis fait");
            }
        } catch (SQLException | SaisieException e) {
            log.error("Erreur lors de la récupération de la liste d'amis", e);
            throw new DAOException(e.getMessage());
        }
        Amis amisList = new Amis(amis);
        return amisList;
    }

    public List<Amis> findById(int id) {
        log.info("Fonction find(id) appelée");
        List<Amis> amis = new ArrayList<>();
        String selectSQL =
                        "SELECT * " +
                        "FROM amis a " +
                        "JOIN utilisateur u ON a.USER_ID_amiDe = u.USER_ID " +
                        "WHERE a.USER_ID_utilisateur = ? " +
                        "AND a.AMIS_STATUT = 'AMI'";

        try {
            PreparedStatement pstmt = connect.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Amis ami = new Amis(
                        rs.getInt("MG_ID"),
                        rs.getInt("USER_ID_utilisateur"),
                        rs.getInt("USER_ID_amiDe"),
                        rs.getDate("AMIS_DATE_DEMANDE"),
                        StatutAmitie.valueOf(rs.getString("AMIS_STATUT"))
                );

                User user = new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_NAME")
                );

                ami.setUser(user);
                amis.add(ami);
            }
            log.info("Liste des amis récupérée avec succès");
        } catch (SQLException e) {
            log.error("Erreur lors de la récupération de la liste d'amis", e);
            throw new DAOException(e.getMessage());
        }

        return amis;
    }

    /**
     * @return
     */
    @Override
    public List<Amis> findAll() {
        List<Amis> amis = new ArrayList<>();
        String sql = "SELECT * FROM amis";

        try(PreparedStatement ps = connect.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Amis ami = new Amis();
                ami.setIdGroupeMessagesPrives(rs.getInt("MG_ID"));
                ami.setStatut(StatutAmitie.valueOf(rs.getString("AMIS_STATUT")));
                ami.setDateDemande(rs.getDate("AMIS_DATE_DEMANDE"));
                ami.setUserIdAmiDe(rs.getInt("USER_ID_amiDe"));
                ami.setUserIdDemandeur(rs.getInt("USER_ID_utilisateur"));
                amis.add(ami);
            }
            return amis;
        }catch (SQLException | SaisieException e){
            log.error("Erreur lors de la recherche de tous les amis", e);
            throw new DAOException(e.getMessage());
        }
    }

    public Amis findByUsers(int userIdDemandeur, int userIdAmiDe) {
        String sql = "SELECT * FROM amis WHERE USER_ID_utilisateur = ? AND USER_ID_amiDe = ?";
        try(PreparedStatement ps = connect.prepareStatement(sql)){


            ps.setInt(1, userIdDemandeur);
            ps.setInt(2, userIdAmiDe);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Amis(
                        rs.getInt("MG_ID"),
                        rs.getInt("USER_ID_utilisateur"),
                        rs.getInt("USER_ID_amiDe"),
                        rs.getDate("AMIS_DATE_DEMANDE"),
                        StatutAmitie.valueOf(rs.getString("AMIS_STATUT"))
                );
            }
        }catch (SQLException e){
            log.error("Erreur lors de la recherche de tous les amis", e);
            throw new DAOException(e.getMessage());
        }
        return null;
    }

    /**
     * Dès, l'utilisateur refuse une demande d'amis, il supprime la ligne de la table amis.
     * Supprime un enregistrement d'amis de la base de données.
     * "DELETE FROM amis WHERE USER_ID_amiDe = ? AND USER_ID_utilisateur = ?"
     *
     * @param obj
     */
    public void refuserDemandeAmisOuRetireEnAmis(Amis obj) {
        if(obj.getIdGroupeMessagesPrives() != null) {
            String deleteSQL = "DELETE FROM groupe_messages_prives WHERE MG_ID = ?";
            try {
                PreparedStatement pstmt = connect.prepareStatement(deleteSQL);
                pstmt.setInt(1, obj.getIdGroupeMessagesPrives());
                pstmt.executeUpdate();
                log.info("Suppression de la demande d'amis directement dans le groupe de messages privés {}", obj.getIdGroupeMessagesPrives());

            } catch (SQLException e) {
                log.error("Erreur lors de la deletion Amis", e);
                throw new DAOException(e.getMessage());
            }
        } else {
            String deleteSQL1 = "DELETE FROM amis WHERE USER_ID_amiDe = ? AND USER_ID_utilisateur = ? OR USER_ID_amiDe = ? AND USER_ID_utilisateur = ?";
            try {
                PreparedStatement pstmt = connect.prepareStatement(deleteSQL1);
                pstmt.setInt(1, obj.getUserIdAmiDe());
                pstmt.setInt(2, obj.getUserIdDemandeur());
                pstmt.setInt(3, obj.getUserIdDemandeur());
                pstmt.setInt(4, obj.getUserIdAmiDe());
                pstmt.executeUpdate();
                log.info("Suppression de la demande d'amis {} et {}", obj.getUserIdDemandeur(), obj.getUserIdAmiDe());
            } catch (SQLException e) {
                log.error("Erreur lors de la deletion Amis", e);
                throw new DAOException(e.getMessage());
            }
        }



    }

    /**
     * Méthode pour accepter une demande d'amis
     * @param obj
     */
    public void UtilisateurAccepteLaDemande(Amis obj) {
        try{
            boolean confirmation = accepterDemandeAmis(obj.getUserIdDemandeur(), obj.getUserIdAmiDe(), obj.getIdGroupeMessagesPrives());
            if(confirmation) {
                dupliquerAmis(obj.getUserIdDemandeur(), obj.getUserIdAmiDe(), obj.getIdGroupeMessagesPrives());
            }
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
    public boolean accepterDemandeAmis(int idDemandeur, int idDemandeAccepter, int idGroupeMessagesPrives) {
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
            return true;
        } catch (SQLException e) {
            log.error("Erreur lors de l'acceptation de la demande d'amis", e);
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Méthode pour dupliquer une ligne d'amis afin de faciliter la recherche de l'utilisateur
     * @param idDemandeur
     * @param idDemandeAccepter
     * @param idGroupeMessagesPrives
     */
    public void dupliquerAmis(int idDemandeur, int idDemandeAccepter, int idGroupeMessagesPrives) {
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

    /**
     * Recupere tout les demandes d'amis de l'utilisateur
     */
    public List<Amis> findAllFriendsRequestById(int id) {
        log.info("Fonction findAllFriendsRequestById(id) appelée");
        List<Amis> amis = new ArrayList<>();
        String selectSQL =
                "SELECT * " +
                        "FROM amis a " +
                        "JOIN utilisateur u " +
                        "ON a.USER_ID_utilisateur = u.USER_ID " +
                        "WHERE a.USER_ID_amiDe = ? " +
                        "AND AMIS_STATUT = 'EN_ATTENTE'";

        try {
            PreparedStatement pstmt = connect.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Amis ami = new Amis(
                        rs.getInt("MG_ID"),
                        rs.getInt("USER_ID_utilisateur"),
                        rs.getInt("USER_ID_amiDe"),
                        rs.getDate("AMIS_DATE_DEMANDE"),
                        StatutAmitie.valueOf(rs.getString("AMIS_STATUT"))
                );

                User user = new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_NAME")
                );

                ami.setUser(user);
                amis.add(ami);
            }
            log.info("Liste des amis récupérée avec succès");
        } catch (SQLException e) {
            log.error("Erreur lors de la récupération de la liste d'amis", e);
            throw new DAOException(e.getMessage());
        }

        return amis;
    }

    //Recherche un amis dans la liste d'amis de l'utilisateur
    public List<Amis> TrouverUnAmis(String name, int id) {
        log.info("Fonction TrouverUnAmis(name, id) appelée");
        List<Amis> amis = new ArrayList<>();
        String selectSQL =
                "SELECT * " +
                        "FROM amis a " +
                        "JOIN utilisateur u " +
                        "ON a.USER_ID_amiDe = u.USER_ID " +
                        "WHERE a.USER_ID_utilisateur = ? " +
                        "AND AMIS_STATUT = 'AMI' " +
                        "AND u.USER_NAME LIKE ?";

        try {
            PreparedStatement pstmt = connect.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            pstmt.setString(2, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Amis ami = new Amis(
                        rs.getInt("MG_ID"),
                        rs.getInt("USER_ID_utilisateur"),
                        rs.getInt("USER_ID_amiDe"),
                        rs.getDate("AMIS_DATE_DEMANDE"),
                        StatutAmitie.valueOf(rs.getString("AMIS_STATUT"))
                );

                User user = new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USER_NAME")
                );

                ami.setUser(user);
                amis.add(ami);
            }
            log.info("Liste des amis récupérée avec succès");
        } catch (SQLException e) {
            log.error("Erreur lors de la récupération de la liste d'amis", e);
            throw new DAOException(e.getMessage());
        }

        return amis;
    }

    //Recupere les infos grâce un id de groupe messages privee
    public Amis findByIdGroupeMessagesPrivee(int id) {
        log.info("Fonction findByIdGroupeMessagesPrivee(id) appelée");
        Amis amis = null;
        String selectSQL =
                "SELECT * " +
                        "FROM amis a " +
                        "WHERE a.MG_ID = ?";

        try {
            PreparedStatement pstmt = connect.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                amis = new Amis();
                amis.setIdGroupeMessagesPrives(rs.getInt("MG_ID"));
                amis.setUserIdDemandeur(rs.getInt("USER_ID_utilisateur"));
                amis.setUserIdAmiDe(rs.getInt("USER_ID_amiDe"));
                amis.setDateDemande(rs.getDate("AMIS_DATE_DEMANDE"));
                amis.setStatut(StatutAmitie.valueOf(rs.getString("AMIS_STATUT")));
            }
            return amis;
        } catch (SQLException e) {
            log.error("Erreur lors de la récupération de la liste d'amis", e);
            throw new DAOException(e.getMessage());
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }

    }
}
