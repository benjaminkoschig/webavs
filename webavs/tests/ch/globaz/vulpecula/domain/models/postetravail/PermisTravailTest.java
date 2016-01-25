package ch.globaz.vulpecula.domain.models.postetravail;

import static org.junit.Assert.*;
import org.junit.Test;

public class PermisTravailTest {
    @Test
    public void given0WhenFromValueShouldReturnBusinessException() {
        try {
            PermisTravail.fromValue("0");
            fail("La valeur 0 est invalide et aucune exception n'a été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void givenTestWhenFromValueShouldReturnBusinessException() {
        try {
            PermisTravail.fromValue("Test");
            fail("La valeur Test est invalide et aucune exceptionn n'a été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void given68004001WhenFromValueShouldBeValid() {
        PermisTravail.fromValue("68004001");
    }
}
