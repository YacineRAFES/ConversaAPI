import fr.afpa.dev.pompey.conversaapi.dao.AmisDAO;
import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.User;
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

    private AmisDAO amisDAO;
    private UserDAO userDAO;
    private User user;

    @BeforeEach
    void setUp() {
        amisDAO = new AmisDAO();
        userDAO = new UserDAO();
        user = new User();
    }

    @AfterEach
    void tearDown() {
        // Clean up resources if needed
    }

    @Test
    void create() {
        // Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword123!%", "johndoe@mail.com", "user", Date.valueOf(LocalDate.now()));
        User user2 = new User("MrBean", "ValidPassword123!%", "MrBean@mail.com", "user", Date.valueOf(LocalDate.now()));
        int iduser1 = userDAO.create(user1);
        int iduser2 = userDAO.create(user2);
        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(iduser1, iduser2);
        boolean confirmation = amisDAO.createDemandeAmis(amis);
        assertTrue(confirmation, "Erreur lors de la création d'une amitié");
        log.info("Demande d'amis confirmée");

        //Supprimez la demande d'amis et les utilisateurs
        amisDAO.delete(amis);
        userDAO.delete(new User(iduser1));
        userDAO.delete(new User(iduser2));
    }

    @Test
    void delete() {
// Crée deux nouveaux utilisateurs
        User user1 = new User("JohnDoe", "ValidPassword123!%", "johndoe@mail.com", "user", Date.valueOf(LocalDate.now()));
        User user2 = new User("MrBean", "ValidPassword123!%", "MrBean@mail.com", "user", Date.valueOf(LocalDate.now()));
        int iduser1 = userDAO.create(user1);
        int iduser2 = userDAO.create(user2);
        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(iduser1, iduser2);
        amisDAO.createDemandeAmis(amis);
        boolean confirmation = amisDAO.delete(amis);
        assertTrue(confirmation, "Erreur lors de la suppression Amis");
        log.info("Demande d'amis confirmée");

        //Supprimez les utilisateurs
        userDAO.delete(new User(iduser1));
        userDAO.delete(new User(iduser2));
    }

    @Test
    void update() {
        User user1 = new User("JohnDoe", "ValidPassword123!%", "johndoe@mail.com", "user", Date.valueOf(LocalDate.now()));
        User user2 = new User("MrBean", "ValidPassword123!%", "MrBean@mail.com", "user", Date.valueOf(LocalDate.now()));
        int iduser1 = userDAO.create(user1);
        int iduser2 = userDAO.create(user2);
        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(iduser1, iduser2);
        amisDAO.createDemandeAmis(amis);
        // Met à jour la demande d'amis
        boolean confirmation = amisDAO.update(amis);
        assertTrue(confirmation, "Erreur lors de la mise à jour de la demande d'amis");
        log.info("Demande d'amis confirme");
        // Supprimez la demande d'amis et les utilisateurs
        log.info("Demande de retirer en amis...");
        amisDAO.delete(amis);
        log.info("Demande de retirer en amis : OK");
        log.info("Suppression des utilisateurs...");
        log.info("Suppression de l'utilisateur " + iduser1);
        userDAO.delete(new User(iduser1));
        log.info("Suppression de l'utilisateur " + iduser2);
        userDAO.delete(new User(iduser2));
        log.info("Suppression des utilisateurs : OK");
    }

    @Test
    void find() {
        User user1 = new User("johndoe", "ValidPassword123!%", "johndoe@mail.com", "user", Date.valueOf(LocalDate.now()));
        User user2 = new User("mrbean", "ValidPassword123!%", "mrbean@mail.com", "user", Date.valueOf(LocalDate.now()));
        int iduser1 = userDAO.create(user1);
        int iduser2 = userDAO.create(user2);
        // Crée une nouvelle amitié entre les deux utilisateurs
        Amis amis = new Amis(iduser1, iduser2);
        amisDAO.createDemandeAmis(amis);

        amisDAO.update(amis);

        // Trouve l'amitié entre les deux utilisateurs
        Amis amisFind = amisDAO.find(iduser1);
        assertTrue(amisFind != null, "Erreur lors de la recherche d'amis");
        log.info("Demande d'amis trouvée");

        log.info("Demande de retirer en amis...");
        amisDAO.delete(amis);
        log.info("Demande de retirer en amis : OK");
        log.info("Suppression des utilisateurs...");
        log.info("Suppression de l'utilisateur " + iduser1);
        userDAO.delete(new User(iduser1));
        log.info("Suppression de l'utilisateur " + iduser2);
        userDAO.delete(new User(iduser2));
        log.info("Suppression des utilisateurs : OK");

    }

    @Test
    void findAll() {
        User user1 = new User("johndoe", "ValidPassword123!%", "johndoe@mail.com", "user", Date.valueOf(LocalDate.now()));
        User user2 = new User("mrbean", "ValidPassword123!%", "mrbean@mail.com", "user", Date.valueOf(LocalDate.now()));
        int iduser1 = userDAO.create(user1);
        int iduser2 = userDAO.create(user2);

        Amis amis = new Amis(iduser1, iduser2);
        amisDAO.createDemandeAmis(amis);
        amisDAO.update(amis);

        List<Amis> amisList = amisDAO.findAll();
        assertTrue(amisList != null, "Erreur lors de la recherche d'amis");
        log.info("Demande d'amis trouvée");

        log.info("Demande de retirer en amis...");
        amisDAO.delete(amis);
        log.info("Demande de retirer en amis : OK");
        log.info("Suppression des utilisateurs...");

        log.info("Suppression de l'utilisateur " + iduser1);
        userDAO.delete(new User(iduser1));

        log.info("Suppression de l'utilisateur " + iduser2);
        userDAO.delete(new User(iduser2));

        log.info("Suppression des utilisateurs : OK");

    }
}
