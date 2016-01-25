package ch.globaz.vulpecula.domain.models.common;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;

public class TauxTest {
    @Test
    public void given1NegativeShouldBeValid() {
        new Taux(-1);
    }

    @Test
    public void given5ShouldBeValid() {
        new Taux(5);
    }

    @Test
    public void given5WhenValueShouldBe5() {
        try {
            String actual = new Taux(5.0).getValue();
            Assert.assertEquals("5.00", actual);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void givenTwo1WhenEqualsShouldBeTrue() {
        Taux taux = new Taux(1);
        Taux taux2 = new Taux(1);

        assertEquals(true, taux.equals(taux2));
    }

    @Test
    public void given2AsStringWhenValueShouldBe2() {
        new Taux("2");
    }

    @Test
    public void givenAAsStringWhenValueShouldBeInvalid() {
        try {
            new Taux("A");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void given100Point00ShouldBeValid() {
        new Taux("100.00");
    }

    @Test
    public void given1MinusAsStringWhenValueShouldBeInvalid() {
        try {
            new Taux("-1");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void twoSameValuesMustBeEqual() {
        Taux taux = new Taux(1);
        Taux taux2 = new Taux(1);

        assertEquals(taux, taux2);
    }

    @Test
    public void isZero_Given12_ShouldBeFalse() {
        assertFalse(new Taux(12).isZero());
    }

    @Test
    public void isZero_Given0_ShouldBeTrue() {
        assertTrue(new Taux(0).isZero());
    }

    @Test
    public void getValueWith2_Given5_ShouldBe5_00() {
        assertThat(new Taux(12).getValueWith(2), is("12.00"));
    }

    @Test
    public void compareTo_Given4_7And27_ShouldBe_1() {
        Taux taux = new Taux(4.7);
        Taux taux2 = new Taux(27);

        assertThat(taux.compareTo(taux2), is(-1));
    }

    @Test
    public void greaterThan_Given80And100_ShouldBeFalse() {
        Taux taux = new Taux(80);
        assertFalse(taux.greaterThan(100));
    }

    @Test
    public void greaterThan_Given101And100_ShouldBeTrue() {
        Taux taux = new Taux(101);
        assertTrue(taux.greaterThan(100));
    }

    @Test
    public void isValid_GivenEmpty_ShouldBeFalse() {
        assertFalse(Taux.isValid(""));
    }

    @Test
    public void isValid_Given1_ShouldBeTrue() {
        assertTrue(Taux.isValid("1"));
    }

    @Test
    public void getValue_Given11_9327_ShouldBe11_93() {
        assertThat(new Taux(11.9327).getValue(), is("11.93"));
    }
}
