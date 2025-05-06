import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
import fr.afpa.dev.pompey.conversaapi.modele.Signalements;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.AmisService;
import fr.afpa.dev.pompey.conversaapi.service.MessagesPriveeService;
import fr.afpa.dev.pompey.conversaapi.service.SignalementsService;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SignalementsDAOTest {
    private AmisService amisService;
    private UserService userService;
    private MessagesPriveeService messagesPriveeService;
    private SignalementsService signalementsService;

    @BeforeEach
    void setUp() {
        amisService = new AmisService(Role.SUPERADMIN);
        userService = new UserService(Role.SUPERADMIN);
        messagesPriveeService = new MessagesPriveeService(Role.SUPERADMIN);
        signalementsService = new SignalementsService(Role.SUPERADMIN);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void add() {
        log.info("addSignalement");
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User userCreated1 = new User(userService.add(user1));
        User userCreated2 = new User(userService.add(user2));

        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(userCreated1.getId(), userCreated2.getId());
        amisService.add(amis);
        Amis amisFind = amisService.find(userCreated1.getId(), userCreated2.getId());
        // Met à jour la demande d'amis
        amisService.update(amisFind);
        // Crée un message privé entre les deux utilisateurs
        log.info(amisFind.getIdGroupeMessagesPrives().toString(), "ID du groupe de messages privés");

        MessagesPrivee messagesPrivee = new MessagesPrivee(
                "Bonjour, comment ça va ?",
                userCreated1,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idmp = messagesPriveeService.add(messagesPrivee);

        MessagesPrivee messagesPrivee1 = messagesPriveeService.findById(idmp);

        // Crée un signalement pour le message privé
        Signalements signalement = new Signalements(
                messagesPrivee1,
                userCreated2
        );

        signalementsService = new SignalementsService(Role.UTILISATEUR);

        boolean confirmation = signalementsService.add(signalement);

        assertTrue(confirmation, "Erreur lors de la création d'un signalement");

        log.info("Signalement créé avec succès");

        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));
    }

    @Test
    void delete() {
        log.info("deleteSignalement");
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User userCreated1 = new User(userService.add(user1));
        User userCreated2 = new User(userService.add(user2));

        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(userCreated1.getId(), userCreated2.getId());
        amisService.add(amis);
        Amis amisFind = amisService.find(userCreated1.getId(), userCreated2.getId());
        // Met à jour la demande d'amis
        amisService.update(amisFind);
        // Crée un message privé entre les deux utilisateurs
        log.info(amisFind.getIdGroupeMessagesPrives().toString(), "ID du groupe de messages privés");

        MessagesPrivee messagesPrivee = new MessagesPrivee(
                "Bonjour, comment ça va ?",
                userCreated1,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idmp = messagesPriveeService.add(messagesPrivee);

        MessagesPrivee messagesPrivee1 = messagesPriveeService.findById(idmp);

        // Crée un signalement pour le message privé
        Signalements signalement = new Signalements(
                messagesPrivee1,
                userCreated2
        );

        signalementsService = new SignalementsService(Role.UTILISATEUR);

        signalementsService.add(signalement);

        signalementsService = new SignalementsService(Role.SUPERADMIN);

        // Supprime le signalement
        boolean confirmation = signalementsService.delete(signalement);

        assertTrue(confirmation, "Erreur lors de la suppression d'un signalement");

        log.info("Signalement supprimée avec succès");

        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));
    }

    @Test
    void update() {
        log.info("updateSignalement");
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User userCreated1 = new User(userService.add(user1));
        User userCreated2 = new User(userService.add(user2));

        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(userCreated1.getId(), userCreated2.getId());
        amisService.add(amis);
        Amis amisFind = amisService.find(userCreated1.getId(), userCreated2.getId());
        // Met à jour la demande d'amis
        amisService.update(amisFind);
        // Crée un message privé entre les deux utilisateurs
        log.info(amisFind.getIdGroupeMessagesPrives().toString(), "ID du groupe de messages privés");

        MessagesPrivee messagesPrivee = new MessagesPrivee(
                "Bonjour, comment ça va ?",
                userCreated1,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idmp = messagesPriveeService.add(messagesPrivee);

        MessagesPrivee messagesPrivee1 = messagesPriveeService.findById(idmp);

        // Crée un signalement1 pour le message privé
        Signalements signalement1 = new Signalements(
                messagesPrivee1,
                userCreated2
        );

        signalementsService = new SignalementsService(Role.UTILISATEUR);

        signalementsService.add(signalement1);

        //Modifier le signalement
        Signalements signalementUpdate = new Signalements(
                messagesPrivee1,
                "Insulte"
        );

        signalementsService = new SignalementsService(Role.SUPERADMIN);

        boolean confirmation = signalementsService.update(signalementUpdate);

        assertTrue(confirmation, "Erreur lors de la modification d'un signalement");

        log.info("Signalement modifié avec succès");

        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));
    }

    @Test
    void getByIdMP() {
        log.info("getByIdMPSignalement");
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User userCreated1 = new User(userService.add(user1));
        User userCreated2 = new User(userService.add(user2));

        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(userCreated1.getId(), userCreated2.getId());
        amisService.add(amis);
        Amis amisFind = amisService.find(userCreated1.getId(), userCreated2.getId());
        // Met à jour la demande d'amis
        amisService.update(amisFind);
        // Crée un message privé entre les deux utilisateurs
        log.info(amisFind.getIdGroupeMessagesPrives().toString(), "ID du groupe de messages privés");

        MessagesPrivee messagesPrivee = new MessagesPrivee(
                "Bonjour, comment ça va ?",
                userCreated1,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idmp = messagesPriveeService.add(messagesPrivee);

        MessagesPrivee messagesPrivee1 = messagesPriveeService.findById(idmp);

        // Crée un signalement1 pour le message privé
        Signalements signalement1 = new Signalements(
                messagesPrivee1,
                userCreated2
        );

        signalementsService = new SignalementsService(Role.UTILISATEUR);

        signalementsService.add(signalement1);

        signalementsService = new SignalementsService(Role.SUPERADMIN);

        //Trouver le signalement

        Signalements signalements = signalementsService.getByIdMP(idmp);

        assertNotNull(signalements, "Erreur lors de la récupération du signalement");

        log.info("Récupération du signalement réussie");

        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));
    }

    @Test
    void getAll() {
        log.info("getAllSignalement");
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User userCreated1 = new User(userService.add(user1));
        User userCreated2 = new User(userService.add(user2));

        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(userCreated1.getId(), userCreated2.getId());
        amisService.add(amis);
        Amis amisFind = amisService.find(userCreated1.getId(), userCreated2.getId());
        // Met à jour la demande d'amis
        amisService.update(amisFind);
        // Crée un message privé entre les deux utilisateurs
        log.info(amisFind.getIdGroupeMessagesPrives().toString(), "ID du groupe de messages privés");

        MessagesPrivee messagesPrivee = new MessagesPrivee(
                "Bonjour, comment ça va ?",
                userCreated1,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idmp = messagesPriveeService.add(messagesPrivee);

        MessagesPrivee messagesPrivee1 = messagesPriveeService.findById(idmp);

        // Crée un signalement1 pour le message privé
        Signalements signalement1 = new Signalements(
                messagesPrivee1,
                userCreated2
        );

        signalementsService = new SignalementsService(Role.UTILISATEUR);

        signalementsService.add(signalement1);

        signalementsService = new SignalementsService(Role.SUPERADMIN);

        //Trouver le signalement

        List<Signalements> signalements = signalementsService.getAll();

        assertNotNull(signalements, "Erreur lors de la récupération de toutes les signalements");

        log.info("Récupération de tous les signalements réussie");

        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));

    }
}