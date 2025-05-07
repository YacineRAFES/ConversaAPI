import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.AmisService;
import fr.afpa.dev.pompey.conversaapi.service.MessagesPriveeService;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MessagesPriveeDAOTest {
    private AmisService amisService;
    private UserService userService;
    private MessagesPriveeService messagesPriveeService;

    @BeforeEach
    void setUp() {
        amisService = new AmisService(Role.UTILISATEUR);
        userService = new UserService(Role.UTILISATEUR);
        messagesPriveeService = new MessagesPriveeService(Role.UTILISATEUR);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        log.info("createTest");
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", UUID.randomUUID() + "@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", UUID.randomUUID() + "@example.com", "user", Date.valueOf(LocalDate.now()), true);
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

        int confirmation = messagesPriveeService.add(messagesPrivee);
        assertTrue(confirmation > 0, "Erreur lors de la création d'un message privé");
        log.info("Création d'un message privé réussi");

        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));
    }

    @Test
    void delete() {
        log.info("deleteTest");
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", UUID.randomUUID() + "@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", UUID.randomUUID() + "@example.com", "user", Date.valueOf(LocalDate.now()), true);
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

        int idMessage = messagesPriveeService.add(messagesPrivee);

        MessagesPrivee messagesPrivee1 = messagesPriveeService.findById(idMessage);

        boolean confirmation = messagesPriveeService.delete(messagesPrivee1);

        assertTrue(confirmation, "Erreur lors de la suppression d'un message privé");
        log.info("Suppression d'un message privé réussi");

        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));

    }

    @Test
    void update() {
        log.info("updateTest");
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", UUID.randomUUID() + "@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", UUID.randomUUID() + "@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User userCreated1 = new User(userService.add(user1));
        User userCreated2 = new User(userService.add(user2));

        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(userCreated1.getId(), userCreated2.getId());
        amisService.add(amis);
        Amis amisFind = amisService.find(userCreated1.getId(), userCreated2.getId());
        // Met à jour la demande d'amis
        amisService.update(amisFind);
        // Crée un message privé entre les deux utilisateurs
        log.info("ID du groupe de messages privés : {}", amisFind.getIdGroupeMessagesPrives());


        MessagesPrivee messagesPrivee = new MessagesPrivee(
                "Bonjour, comment ça va ?",
                userCreated1,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idMessage = messagesPriveeService.add(messagesPrivee);
        log.info("ID MESSAGE : "+idMessage);

        MessagesPrivee messagesPriveeFind = messagesPriveeService.findById(idMessage);
        log.info("ID du message privé : {}", messagesPriveeFind.getId());


        MessagesPrivee messagesPrivee2 = new MessagesPrivee(
                "Bonjour, comment allez-vous ?",
                messagesPriveeFind.getId()
        );

        boolean confirmation = messagesPriveeService.update(messagesPrivee2);

        assertTrue(confirmation, "Erreur lors de la modification d'un message privé");
        log.info("Modification d'un message privé réussi");

        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));
    }

    @Test
    void find() {
        log.info("findTest");
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
        log.info("ID du groupe de messages privés : {}", amisFind.getIdGroupeMessagesPrives());


        MessagesPrivee messagesPrivee = new MessagesPrivee(
                "Bonjour, comment ça va ?",
                userCreated1,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idMessage = messagesPriveeService.add(messagesPrivee);
        log.info("ID MESSAGE : "+idMessage);

        MessagesPrivee messagesPrivee1 = messagesPriveeService.findById(idMessage);

        assertTrue(messagesPrivee1.getId() > 0, "Erreur lors de trouver d'un message privé");
        log.info("Trouver un message privée réussi");
        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));
    }

    @Test
    void findAll() {
        log.info("findAllTest");
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
        log.info("ID du groupe de messages privés : {}", amisFind.getIdGroupeMessagesPrives());


        MessagesPrivee messagesPrivee1 = new MessagesPrivee(
                "Bonjour, comment allez-vous ?",
                userCreated1,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idMessage1 = messagesPriveeService.add(messagesPrivee1);
        log.info("ID MESSAGE : "+idMessage1);

        MessagesPrivee messagesPrivee2 = new MessagesPrivee(
                "Bonjour, ça va et vous?",
                userCreated2,
                amisFind.getIdGroupeMessagesPrives()
        );

        int idMessage2 = messagesPriveeService.add(messagesPrivee2);
        log.info("ID MESSAGE : "+idMessage2);

        List<MessagesPrivee> messagesPriveeList = messagesPriveeService.getAll();

        assertTrue(messagesPriveeList != null, "Erreur lors de trouver d'un message privé");
        log.info("Trouver des messages privées réussi");
        amisService.delete(amisFind);

        userService.delete(new User(userCreated1.getId()));
        userService.delete(new User(userCreated2.getId()));
    }
}