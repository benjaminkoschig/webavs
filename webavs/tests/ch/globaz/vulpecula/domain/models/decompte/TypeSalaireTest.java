package ch.globaz.vulpecula.domain.models.decompte;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeSalaireTest {
    @Test
    public void given0WhenFromValueShouldReturnBusinessException() {
        try {
            TypeSalaire.fromValue("0");
            fail("La valeur 0 est invalide et aucune exception n'a été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void givenTestWhenFromValueShouldReturnBusinessException() {
        try {
            TypeSalaire.fromValue("Test");
            fail("La valeur Test est invalide et aucune exceptionn n'a été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void given68013001WhenFromValueShouldBeValid() {
        TypeSalaire.fromValue("68013001");
    }

    @Test
    public void given0WhenIsValidShouldReturnFalse() {
        boolean actual = TypeSalaire.isValid("0");
        assertFalse(actual);
    }
}
