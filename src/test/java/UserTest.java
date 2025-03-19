import fr.afpa.dev.pompey.modele.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User userUnderTest;

    @BeforeEach
    void setUp() {
        userUnderTest = new User();
    }


    @Test
    void setId() {
    }

    @ParameterizedTest
    @NullSource
    void setNameNullSource(String name) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setName(null);
        });
        assertEquals("Le nom ne doit pas être null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setNameEmptySource(String name) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setName("");
        });
        assertEquals("Le nom ne doit pas être vide", exception.getMessage());
    }

    @Test
    void setPassword() {
    }

    @ParameterizedTest
    @NullSource
    void setPasswordNullSource(String password) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setPassword(null);
        });
        assertEquals("Le mot de passe ne doit pas être null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setPasswordEmptySource(String password) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setPassword("");
        });
        assertEquals("Le mot de passe ne doit pas être vide", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setEmailNullSource(String email) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setEmail(null);
        });
        assertEquals("L'email ne doit pas être null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setEmailEmptySource(String email) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setEmail("");
        });
        assertEquals("L'email ne doit pas être vide", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setRoleNullSource(String role) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setRole(null);
        });
        assertEquals("Le role ne doit pas être null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setRoleEmptySource(String role) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setRole("");
        });
        assertEquals("Le role ne doit pas être vide", exception.getMessage());
    }

    @Test
    void setDate() {
    }

    @ParameterizedTest
    @NullSource
    void setDateNullSource(Date date) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setDate(null);
        });
        assertEquals("La date ne doit pas être null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setDateEmptySource(Date date) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUnderTest.setDate(null);
        });
        assertEquals("La date ne doit pas être vide", exception.getMessage());
    }

    @AfterEach
    void tearDown() {
    }
}