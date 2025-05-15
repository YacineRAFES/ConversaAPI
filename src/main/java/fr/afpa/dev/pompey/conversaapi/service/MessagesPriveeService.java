package fr.afpa.dev.pompey.conversaapi.service;

import fr.afpa.dev.pompey.conversaapi.dao.DAOFactory;
import fr.afpa.dev.pompey.conversaapi.dao.MessagesPriveeDAO;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;

import java.util.List;

import static fr.afpa.dev.pompey.conversaapi.utilitaires.Utils.getNameClass;

public class MessagesPriveeService {

    private final MessagesPriveeDAO messagesPriveeDAO;

    /**
     * Constructeur de la classe MessagesPriveeService.
     *
     * @param role Le rôle de l'utilisateur (UTILISATEUR, MODERATEUR OU SUPERADMIN).
     */
    public MessagesPriveeService(Role role) {
        DAOFactory daoFactory = new DAOFactory(role);
        this.messagesPriveeDAO = daoFactory.getMessagesPriveeDAO();
    }

    /**
     * Récupère tous les messages privés de la base de données.
     *
     * @return La liste de tous les messages privés.
     */
    public List<MessagesPrivee> getAll() {
        return messagesPriveeDAO.findAll();
    }

    /**
     * Ajoute un message privé à la base de données.
     *
     * @param messagesPrivee Le message privé à ajouter.
     * @return L'ID du message privé ajouté.
     * @throws IllegalStateException Si une erreur se produit lors de l'ajout.
     */
    public int add(MessagesPrivee messagesPrivee) throws IllegalStateException {
        try {
            return messagesPriveeDAO.createMessagesPrives(messagesPrivee);
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors de l'ajout d'un message privee : " + e.getMessage());
        }
    }



    /**
     * Met à jour un message privé dans la base de données.
     *
     * @param messagesPrivee Le message privé à mettre à jour.
     * @return true si la mise à jour a réussi, false sinon.
     */
    public boolean update(MessagesPrivee messagesPrivee) {
        return messagesPriveeDAO.update(messagesPrivee);
    }

    /**
     * Supprime un message privé de la base de données.
     *
     * @param messagesPrivee Le message privé à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean delete(MessagesPrivee messagesPrivee) {
        return messagesPriveeDAO.delete(messagesPrivee);
    }

    /**
     * Récupère un message privé par son ID.
     *
     * @param id L'ID du message privé à récupérer.
     * @return Le message privé correspondant à l'ID.
     */
    public MessagesPrivee findById(int id) {
        return messagesPriveeDAO.find(id);
    }

    public List<MessagesPrivee> getAllMessagesPriveeByIdUser(Integer id) {
        List<MessagesPrivee> messagesPrivee = messagesPriveeDAO.findAllMessagesPriveesByIdUser(id);
        if (messagesPrivee == null) {
            throw new IllegalStateException(getNameClass() + " : Erreur lors de la récupération des messages privés");
        }
        return messagesPrivee;
    }

//    public MessagesPrivee signaler(MessagesPrivee messagesPrivee) {
//        return messagesPriveeDAO.signalerUnMessage(messagesPrivee);
//    }

    public MessagesPrivee supprimer(MessagesPrivee messagesPrivee) {
        return messagesPriveeDAO.supprimerUnMessage(messagesPrivee);
    }

    public List<MessagesPrivee> findAllMessagesPriveesByIdMG(int idMG){
        return messagesPriveeDAO.findAllMessagesPriveesByIdMG(idMG);
    }
}
