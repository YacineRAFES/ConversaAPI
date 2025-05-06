import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
import fr.afpa.dev.pompey.conversaapi.modele.Signalements;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SignalementsTest {
    private Signalements signalementsUnderTest;

    @BeforeEach
    void setUp() {
        signalementsUnderTest = new Signalements();
    }

    @ParameterizedTest
    @NullSource
    void setMessagesPriveeIsNull(MessagesPrivee messagesPrivee) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            signalementsUnderTest.setMessagesPrivee(messagesPrivee);
        });
        assertEquals("Le message ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setUserIsNull(User user) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            signalementsUnderTest.setUser(user);
        });
        assertEquals("L'utilisateur ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setDate() {
        LocalDate localDate = LocalDate.of(2020, 1, 1);
        LocalDate actuelDate = LocalDate.of(2020, 1, 1);
        assertEquals(localDate, actuelDate, "La date ne corresponds pas");
    }

    @ParameterizedTest
    @NullSource
    void setRaisonIsNull(String raison) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            signalementsUnderTest.setRaison(raison);
        });
        assertEquals("La raison ne doit pas être vide ou null", exception.getMessage());
    }
}