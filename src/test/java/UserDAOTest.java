import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @AfterEach
    void tearDown() {
        // Nettoyez les utilisateurs créés pendant les tests si nécessaire
    }

    @Test
    void create() {
        User user = new User("John Doe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()));
        int id = userDAO.create(user);
        assertTrue(id > 0, "Erreur lors de la création User");
        log.info("L'id de l'utilisateur inséré est " + id);

        // Supprimez l'utilisateur après le test
        userDAO.delete(new User(id));
    }

    @Test
    void update() throws SaisieException {
        User user = new User(1, "Alice", "ValidPassword1%$!", "alice@example.com", "user", Date.valueOf(LocalDate.now()));
        boolean updated = userDAO.update(user);
        assertTrue(updated, "L'utilisateur n'a pas été mis à jour correctement");
    }

    @Test
    void find() {
        User user = new User("Alice", "ValidPassword1%$!", "alice11@example.com", "user", Date.valueOf(LocalDate.now()));
        int userid = userDAO.create(user);
        User userFind = userDAO.find(userid);
        assertNotNull(userFind, "L'utilisateur n'a pas été trouvé");
        assertEquals("Alice", userFind.getName(), "Le nom de l'utilisateur ne correspond pas");

        // Supprimez l'utilisateur après le test
        userDAO.delete(new User(userid));
    }

    @Test
    void findAll() {
        User user = new User("Test User", "ValidPassword1%$!", "test.user@example.com", "user", Date.valueOf(LocalDate.now()));
        int id = userDAO.create(user);

        assertTrue(id > 0, "Erreur lors de la création de l'utilisateur de test");

        List<User> users = userDAO.findAll();

        assertFalse(users.isEmpty(), "Le nombre d'utilisateurs trouvés est incorrect");

        userDAO.delete(new User(id));
    }

    @Test
    void delete() {
        User user = new User("John Doe", "ValidPassword1%$!", "john.doe@example.com", "user", Date.valueOf(LocalDate.now()));
        int id = userDAO.create(user);
        boolean deleted = userDAO.delete(new User(id));
        assertTrue(deleted, "L'utilisateur n'a pas été supprimé correctement");
    }
}
