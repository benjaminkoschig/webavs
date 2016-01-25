package ch.globaz.vulpecula.domain.models.common;

import static org.junit.Assert.fail;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.EntierPositif;

public class EntierTest {
    @Test
    public void given0ShouldBeInvalid() {
        try {
            new EntierPositif(0);
            Assert.fail("Le nombre 0 n'est pas valide et l'exception n'a pas été levée");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void given1ShouldBeValid() {
        new EntierPositif(1);
    }

    @Test
    public void given5WhenGetNombreHeuresShouldBe5() {
        int actual = new EntierPositif(5).getValue();
        Assert.assertEquals(5, actual);
    }

    @Test
    public void givenTwo5WhenEqualsShouldBeTrue() {
        EntierPositif nombreHeures = new EntierPositif(5);
        EntierPositif nombreHeures2 = new EntierPositif(5);

        Assert.assertEquals(true, nombreHeures.equals(nombreHeures2));
    }

    @Test
    public void given5AsStringShoulBeTrue() {
        new EntierPositif("5");
    }

    @Test
    public void givenMinus5AsString() {
        try {
            new EntierPositif("-5");
            fail("La valeur n'est pas valide et aucune exceptionn n'a été levée");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void givenTestAsString() {
        try {
            new EntierPositif("test");
            fail("La valeur n'est pas valide et aucune exceptionn n'a été levée");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }
}
