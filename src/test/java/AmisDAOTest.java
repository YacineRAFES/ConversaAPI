import fr.afpa.dev.pompey.conversaapi.dao.AmisDAO;
import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.AmisService;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class AmisDAOTest {

    private AmisService amisService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        amisService = new AmisService(Role.UTILISATEUR);
        userService = new UserService(Role.UTILISATEUR);
    }

    @AfterEach
    void tearDown() {
        // Clean up resources if needed
    }

    @Test
    void create() {
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        int id1 = userService.add(user1);
        int id2 = userService.add(user2);

        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(id1, id2);
        boolean confirmation = amisService.add(amis);
        assertTrue(confirmation, "Erreur lors de la création d'une amitié");
        log.info("Demande d'amis confirmée");

        Amis amisFind = amisService.find(id1, id2);

        amisService.delete(amisFind);

        userService.delete(new User(id1));
        userService.delete(new User(id2));
    }

    @Test
    void delete() {
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        int id1 = userService.add(user1);
        int id2 = userService.add(user2);
        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(id1, id2);
        amisService.add(amis);
        boolean confirmation = amisService.delete(amis);
        assertTrue(confirmation, "Erreur lors de la suppression Amis");
        log.info("Demande d'amis confirmée");

        //Supprimez les utilisateurs
        userService.delete(new User(id1));
        userService.delete(new User(id2));
    }

    @Test
    void update() {
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        int id1 = userService.add(user1);
        int id2 = userService.add(user2);
        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(id1, id2);
        amisService.add(amis);

        Amis amisFind = amisService.find(id1, id2);
        // Met à jour la demande d'amis
        boolean confirmation = amisService.update(amis);
        assertTrue(confirmation, "Erreur lors de la mise à jour de la demande d'amis");
        log.info("Demande d'amis confirme");
        // Supprimez la demande d'amis et les utilisateurs
        log.info("Demande de retirer en amis...");
        amisService.delete(amisFind);
        log.info("Demande de retirer en amis : OK");
        log.info("Suppression des utilisateurs...");
        log.info("Suppression de l'utilisateur " + id1);
        userService.delete(new User(id1));
        log.info("Suppression de l'utilisateur " + id2);
        userService.delete(new User(id2));
        log.info("Suppression des utilisateurs : OK");
    }

    @Test
    void find() {
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        int id1 = userService.add(user1);
        int id2 = userService.add(user2);
        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(id1, id2);
        amisService.add(amis);

        amisService.update(amis);

        // Trouve l'amitié entre les deux utilisateurs
        Amis amisFind = (Amis) amisService.get(id1);
        assertTrue(amisFind != null, "Erreur lors de la recherche d'amis");
        log.info("Demande d'amis trouvée");

        log.info("Demande de retirer en amis...");
        amisService.delete(amis);
        log.info("Demande de retirer en amis : OK");
        log.info("Suppression des utilisateurs...");
        log.info("Suppression de l'utilisateur " + id1);
        userService.delete(new User(id1));
        log.info("Suppression de l'utilisateur " + id2);
        userService.delete(new User(id2));
        log.info("Suppression des utilisateurs : OK");

    }

    @Test
    void findAll() {
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        User user2 = new User("Aliiiice", "VdfdalidPassword1%$!", "aliiiice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        int id1 = userService.add(user1);
        int id2 = userService.add(user2);
        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(id1, id2);
        amisService.add(amis);

        amisService.update(amis);

        List<Amis> amisList = amisService.getAll();
        assertTrue(amisList != null, "Erreur lors de la recherche d'amis");
        log.info("Demande d'amis trouvée");

        log.info("Demande de retirer en amis...");
        amisService.delete(amis);
        log.info("Demande de retirer en amis : OK");
        log.info("Suppression des utilisateurs...");

        log.info("Suppression de l'utilisateur " + id1);
        userService.delete(new User(id1));

        log.info("Suppression de l'utilisateur " + id2);
        userService.delete(new User(id2));

        log.info("Suppression des utilisateurs : OK");

    }
}
