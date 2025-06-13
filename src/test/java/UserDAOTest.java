import fr.afpa.dev.pompey.conversaapi.dao.DAOFactory;
import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
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
class UserDAOTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(Role.UTILISATEUR);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        User user = new User(
                "JohnDoe",
                "ValidPassword1%$!",
                "john.doe@example.com",
                "user",
                Date.valueOf(LocalDate.now()),
                true
        );
        int newID = userService.add(user);

        User userFind = userService.get(newID);

        assertNotNull(newID, "L'utilisateur n'a pas été ajouté correctement");
        log.info("Utilisateur ajouté : " + userFind.getName());

        // Supprimez l'utilisateur après le test
        userService.delete(userFind);
    }

    @Test
    void update() {
        // Créez un utilisateur à mettre à jour
        User user = new User("Aliceeeeee", "ValidPassword1%$!", "alice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        int id = userService.add(user);


        // Update un utilisateur
        User userModif = new User(
                id,
                "AliceUpdated",
                "NewValidPassword1%$!",
                "aliceUpdated@example.com",
                "user",
                Date.valueOf(LocalDate.now()),
                true
                );
        boolean userModifier = userService.update(userModif);

        assertTrue(userModifier, "L'utilisateur n'a pas été mis à jour correctement");

        userService.delete(new User(id));
    }

    @Test
    void find() {
        // Créez un utilisateur à mettre à jour
        User user = new User("Aliceeeeee", "ValidPassword1%$!", "alice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        userService.add(user);

        List<User> users = userService.getAll();
        User findUser = users.stream()
                .filter(u -> u.getName().equals("Aliceeeeee"))
                .findFirst()
                .orElse(null);

        User userFind = userService.get(findUser.getId());
        assertEquals("Aliceeeeee", userFind.getName(), "Le nom n'a pas été trouvé");

        userService.delete(findUser);
    }

    @Test
    void findAll() {
        User user = new User("Aliceeeeee", "ValidPassword1%$!", "alice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        userService.add(user);
        User user1 = new User("joooooohndoe", "ValidPasfdfsword1%$!", "joooooohndoe@example.com", "user", Date.valueOf(LocalDate.now()), true);
        userService.add(user1);

        List<User> users = userService.getAll();

        User findUser1 = users.stream()
                .filter(u -> u.getName().equals("Aliceeeeee"))
                .findFirst()
                .orElse(null);

        User findUser2 = users.stream()
                .filter(u -> u.getName().equals("joooooohndoe"))
                .findFirst()
                .orElse(null);

        assertFalse(users.isEmpty(), "La liste des utilisateurs est vide");

        // Supprimez les utilisateurs après le test
        userService.delete(findUser1);
        userService.delete(findUser2);

    }

    @Test
    void delete() {
        User user = new User("Aliceeeeee", "ValidPassword1%$!", "alice@example.com", "user", Date.valueOf(LocalDate.now()), true);
        userService.add(user);

        List<User> users = userService.getAll();
        User deletedUser = users.stream()
                .filter(u -> u.getName().equals("Aliceeeeee"))
                .findFirst()
                .orElse(null);
        boolean deleted = userService.delete(deletedUser);

        assertTrue(deleted, "L'utilisateur n'a pas été supprimé correctement");
    }
}
