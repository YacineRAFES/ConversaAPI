import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MessagesPriveeTest {
    private MessagesPrivee messagesPriveeUnderTest;

    @BeforeEach
    void setUp() {
        messagesPriveeUnderTest = new MessagesPrivee();
    }

    @ParameterizedTest
    @NullSource
    void setIdNullSource(Integer id) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            messagesPriveeUnderTest.setId(id);
        });
        assertEquals("L'id ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setIdNegative() {
        Exception exception = assertThrows(SaisieException.class, () -> {
            messagesPriveeUnderTest.setId(-1);
        });
        assertEquals("L'id ne doit pas être négatif", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setDateNullSource(Timestamp date) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            messagesPriveeUnderTest.setDate(date);
        });
        assertEquals("La date ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setDateEqual() {
        LocalDate localDate = LocalDate.of(2020, 1, 1);
        LocalDate actuelDate = LocalDate.of(2020, 1, 1);
        assertEquals(localDate, actuelDate, "La date ne corresponds pas");
    }

    @ParameterizedTest
    @NullSource
    void setMessageNullSource(String message) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            messagesPriveeUnderTest.setMessage(message);
        });
        assertEquals("Le message ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    void setMessageEmptySource(String message) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            messagesPriveeUnderTest.setMessage(message);
        });
        assertEquals("Le message ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setUserNullSource(User user) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            messagesPriveeUnderTest.setUser(user);
        });
        assertEquals("L'User ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setIdGroupeMessagesPrivesNullSource(Integer id) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            messagesPriveeUnderTest.setIdGroupeMessagesPrives(id);
        });
        assertEquals("L'idGroupeMessagesPrives ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setIdGroupeMessagesPrivesNegative() {
        Exception exception = assertThrows(SaisieException.class, () -> {
            messagesPriveeUnderTest.setIdGroupeMessagesPrives(-1);
        });
        assertEquals("L'idGroupeMessagesPrives ne doit pas être négatif", exception.getMessage());
    }

}