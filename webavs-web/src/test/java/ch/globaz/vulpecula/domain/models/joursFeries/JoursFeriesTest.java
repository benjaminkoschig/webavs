package ch.globaz.vulpecula.domain.models.joursFeries;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import ch.globaz.vulpecula.domain.models.holidays.JoursFeries;

@Ignore
public class JoursFeriesTest {
    private static JoursFeries joursFeries;

    @BeforeClass
    @Ignore
    public static void before() {
        joursFeries = JoursFeries.getInstance();
    }

    @Test
    @Ignore
    public void isJoursFerie_GivenJoursFerie_ShouldReturnTrue() {
        boolean result = joursFeries.isJourFerie(2015, 12, 25);

        assertTrue(result);
    }

    @Test
    @Ignore
    public void isJoursFerie_GivenJoursNotFerie_ShouldReturnFalse() {
        boolean result = joursFeries.isJourFerie(2015, 04, 12);

        assertFalse(result);
    }
}
