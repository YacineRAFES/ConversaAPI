import fr.afpa.dev.pompey.exception.SaisieException;
import fr.afpa.dev.pompey.modele.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import fr.afpa.dev.pompey.securite.Securite;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User userUnderTest;

    @BeforeEach
    void setUp() {
        userUnderTest = new User();
    }


    @ParameterizedTest
    @NullSource
    void setIdNullSource(Integer id) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setId(null);
        });
        assertEquals("L'id ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setIdNegative() {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setId(-1);
        });
        assertEquals("L'id ne doit pas être négatif", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setNameNullSource(String name) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setName(null);
        });
        assertEquals("Le nom ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setNameEmptySource(String name) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setName("");
        });
        assertEquals("Le nom ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setPasswordNullSource(String password) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setPassword(null);
        });
        assertEquals("Le mot de passe ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setPasswordEmptySource(String password) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setPassword("");
        });
        assertEquals("Le mot de passe ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setPasswordInvalide() {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setPassword("password");
        });
        assertEquals("Le mot de passe ne respecte pas les critères", exception.getMessage());
    }

    @Test
    void setPasswordValide() throws SaisieException {
        String validPassword = "ValidPassword1%$!";
        userUnderTest.setPassword(validPassword);
        assertNotNull(userUnderTest.getPassword());
        assertTrue(Securite.checkPassword(validPassword, userUnderTest.getPassword()));
    }

    @ParameterizedTest
    @NullSource
    void setEmailNullSource(String email) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setEmail(null);
        });
        assertEquals("L'email ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setEmailEmptySource(String email) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setEmail("");
        });
        assertEquals("L'email ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setRoleNullSource(String role) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setRole(null);
        });
        assertEquals("Le role ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setRoleEmptySource(String role) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setRole("");
        });
        assertEquals("Le role ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setDateNullSource(Date date) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            userUnderTest.setDate(null);
        });
        assertEquals("La date ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setDateEqual() {
        LocalDate localDate = LocalDate.of(2020, 1, 1);
        LocalDate actuelDate = LocalDate.of(2020, 1, 1);
        assertEquals(localDate, actuelDate, "La date ne corresponds pas");
    }

    @AfterEach
    void tearDown() {
    }
}