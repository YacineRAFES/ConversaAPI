import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.StatutAmitie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmisTest {
    private Amis amisUnderTest;

    @BeforeEach
    void setUp() {
        amisUnderTest = new Amis();
    }

    @ParameterizedTest
    @NullSource
    void setStatut(StatutAmitie statut) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            amisUnderTest.setStatut(statut);
        });
        assertEquals("Le statut ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setDateDemande() {
        Exception exception = assertThrows(SaisieException.class, () -> {
            amisUnderTest.setDateDemande(null);
        });
        assertEquals("La date ne doit pas être vide ou null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setDateDemandeNullSource(Date dateDemande) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            amisUnderTest.setDateDemande(dateDemande);
        });
        assertEquals("La date ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setDateDemandeEqual() {
        LocalDate localDate = LocalDate.of(2020, 1, 1);
        LocalDate actuelDate = LocalDate.of(2020, 1, 1);
        assertEquals(localDate, actuelDate, "La date ne corresponds pas");
    }

    @ParameterizedTest
    @NullSource
    void setUserIdDemandeurNullSource(Integer id) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            amisUnderTest.setUserIdDemandeur(id);
        });
        assertEquals("L'id ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setUserIdDemandeurNegative() {
        Exception exception = assertThrows(SaisieException.class, () -> {
            amisUnderTest.setUserIdDemandeur(-1);
        });
        assertEquals("L'id ne doit pas être négatif", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setUserIdAmiDeNullSource(Integer id) {
        Exception exception = assertThrows(SaisieException.class, () -> {
            amisUnderTest.setUserIdAmiDe(id);
        });
        assertEquals("L'id ne doit pas être vide ou null", exception.getMessage());
    }

    @Test
    void setUserIdAmiDeNegative() {
        Exception exception = assertThrows(SaisieException.class, () -> {
            amisUnderTest.setUserIdAmiDe(-1);
        });
        assertEquals("L'id ne doit pas être négatif", exception.getMessage());
    }
}