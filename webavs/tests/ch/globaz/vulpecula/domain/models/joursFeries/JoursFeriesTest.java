package ch.globaz.vulpecula.domain.models.joursFeries;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.holidays.JoursFeries;

public class JoursFeriesTest {
    private static JoursFeries joursFeries;

    @BeforeClass
    public static void before() {
        joursFeries = JoursFeries.getInstance();
    }

    @Test
    public void isJoursFerie_GivenJoursFerie_ShouldReturnTrue() {
        boolean result = joursFeries.isJourFerie(2015, 12, 25);

        assertTrue(result);
    }

    @Test
    public void isJoursFerie_GivenJoursNotFerie_ShouldReturnFalse() {
        boolean result = joursFeries.isJourFerie(2015, 04, 12);

        assertFalse(result);
    }
}
