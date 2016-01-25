package ch.globaz.vulpecula.domain.models.postetravail;

import static org.junit.Assert.*;
import org.junit.Test;

public class QualificationTest {
    @Test
    public void given0WhenFromValueShouldReturnBusinessException() {
        try {
            Qualification.fromValue("0");
            fail("La valeur 0 est invalide et aucune exception n'a été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void givenTestWhenFromValueShouldReturnBusinessException() {
        try {
            Qualification.fromValue("Test");
            fail("La valeur Test est invalide et aucune exceptionn n'a été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void given68007001WhenFromValueShouldBeValid() {
        Qualification.fromValue("68007001");
    }

    @Test
    public void given0WhenIsValidThenFalse() {
        boolean actual = Qualification.isValid("0");
        assertFalse(actual);
    }
}
