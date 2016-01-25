package ch.globaz.vulpecula.domain.models.common;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class MontantTest {
    @Test
    public void given100StringShouldBeValid() {
        new Montant("100");
    }

    @Test
    public void given10StringWhenGetValueShouldBe10() {
        String actual = new Montant("10").getValue();

        Assert.assertEquals("10.00", actual);
    }

    @Test
    public void givenInvalidNumberShouldBeInvalid() {
        try {
            new Montant("100..5");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void givenTwoSameMontantWhenEqualsShouldBeTrue() {
        Montant montant = new Montant(1);
        Montant montant2 = new Montant(1);

        Assert.assertEquals(true, montant.equals(montant2));
    }

    @Test
    public void givenFloatWhenScale2() {
        Montant montant = new Montant("10000.2336");
        Assert.assertEquals("10'000.23", montant.toStringFormat());
    }

    @Test
    public void multiply_Given1000And2_ShouldBe2000() {
        Montant montant = new Montant("1000");

        assertEquals("2000.00", montant.multiply(2).getValue());
    }

    @Test
    public void multiply_Given1000And2_5_ShouldBe2000() {
        Montant montant = new Montant("1000");

        assertEquals("2500.00", montant.multiply(new BigDecimal(2.5)).getValue());
    }

    @Test
    public void multiply_Given1000AndTauxOf50_ShouldBe50() {
        Montant montant = new Montant(1000);
        Taux taux = new Taux(50);

        Montant expected = new Montant(500);
        Montant actual = montant.multiply(taux);

        assertEquals(expected, actual);
    }

    @Test
    public void multiply_Given100000AndTauxOf14_35_ShouldB14350() {
        Montant montant = new Montant(100000);
        Taux taux = new Taux(4.35);

        Montant expected = new Montant(4350);
        Montant actual = montant.multiply(taux);

        assertEquals(expected, actual);
    }

    @Test
    public void substract_Given300And100_ShouldBe200() {
        Montant montant = Montant.valueOf(300);
        Montant montantToSubstract = Montant.valueOf(100);

        Montant actual = montant.substract(montantToSubstract);
        assertEquals(Montant.valueOf(200), actual);
    }

    @Test
    public void centime_given100_50() {
        Montant m = new Montant(100.50);
        assertEquals(50, m.getCentimes());
        assertEquals(100, m.getMontantSansCentimes());
    }

    @Test
    public void centime_given0_5() {
        Montant m = new Montant(0.50);
        assertEquals(50, m.getCentimes());
    }

    @Test
    public void centime_given0() {
        Montant m = new Montant(0);
        assertEquals(0, m.getCentimes());
    }

    @Test
    public void centime_given0_0010() {
        Montant m = new Montant(0.0010);
        assertEquals(0, m.getCentimes());
    }

    @Test
    public void centime_given1_0() {
        Montant m = new Montant(1.0);
        assertEquals(0, m.getCentimes());
    }

    @Test
    public void getBigDecimalValueToString_GivenMontantOf10_0001_ShouldBe10_0001() {
        Montant m = new Montant(10.0001);

        assertEquals("10.0001", m.getBigDecimalValue().toString());
    }

    @Test
    public void divide_GivenMontantOf10DivideBy5_ShouldBe2() {
        Montant value = new Montant(10);
        Montant diviser = new Montant(5);
        Montant expected = new Montant(2);

        assertEquals(expected, value.divide(diviser));
    }

    @Test
    public void multiply_GivenMontantOf10By5_ShouldBe50() {
        Montant value = new Montant(10);
        Montant multiplier = new Montant(5);
        Montant expected = new Montant(50);

        assertEquals(expected, value.multiply(multiplier));
    }

    @Test
    public void divideBy100_GivenMontantOf250_ShouldBe2_5() {
        Montant value = new Montant(250);

        assertEquals(new Montant(2.5), value.divideBy100());
    }

    @Test
    public void getCentimes_GivenMontantOf250_13_ShouldBe15() {
        Montant value = new Montant(250.13);

        assertEquals(13, value.getCentimes());
    }

    @Test
    public void getValueNormalisee_GivenMontantOf250_13_ShouldBe15() {
        Montant value = new Montant(250.13);

        assertEquals("250.15", value.getValueNormalisee());
    }

    @Test
    public void getValue_GivenMontantOf250_1354_ShouldBe250_14() {
        Montant value = new Montant(250.1354);

        assertEquals("250.14", value.getValue());
    }

    @Test
    public void getValueNormalisee_GivenMontantOf250_1354_ShouldBe250_15() {
        Montant value = new Montant(250.1354);

        assertEquals("250.15", value.getValueNormalisee());
    }

    @Test
    public void greaterOrEquals2000_Given1500_ShouldBeTrue() {
        assertTrue(new Montant(2000).greaterOrEquals(new Montant(1500)));
    }

    @Test
    public void greaterOrEquals2000_Given2000_ShouldBeTrue() {
        assertTrue(new Montant(2000).greaterOrEquals(new Montant(2000)));
    }

    @Test
    public void greaterOrEquals1500_Given2000_ShouldBeTrue() {
        assertFalse(new Montant(1500).greaterOrEquals(new Montant(2000)));
    }

    @Test
    public void normalize_Given12_24_ShouldBe12_25() {
        assertThat(new Montant(12.24).normalize(), is(new Montant(12.25)));
    }

    @Test
    public void normalize_Given12_21_ShouldBe12_20() {
        assertThat(new Montant(12.21).normalize(), is(new Montant(12.20)));
    }

    @Test
    public void getMontantAbsolu_GivenMinus12_ShouldBe12() {
        assertThat(new Montant(-12).getMontantAbsolu(), is(new Montant(12)));
    }

    @Test
    public void getMontantAbsolu_GivenMinus1_200_ShouldBe1_2() {
        assertThat(new Montant(-1.200).getMontantAbsolu(), is(new Montant(1.2)));
    }

    @Test
    public void getMontantAbsolu_Given12_ShouldBe12() {
        assertThat(new Montant(12).getMontantAbsolu(), is(new Montant(12)));
    }

    @Test
    public void greater2000_Given1500_ShouldBeTrue() {
        assertTrue(new Montant(2000).greater(new Montant(1500)));
    }

    @Test
    public void greater2000_Given2000_ShouldBeFalse() {
        assertFalse(new Montant(2000).greater(new Montant(2000)));
    }

    @Test
    public void greater1500_Given2000_ShouldBeFalse() {
        assertFalse(new Montant(1500).greater(new Montant(2000)));
    }

    @Test
    public void less1500_Given2000_ShouldBeTrue() {
        assertTrue(new Montant(1500).less(new Montant(2000)));
    }

    @Test
    public void divide_FloatsNumber_ShouldNotThrowException() {
        Montant result = new Montant("1243.00").divide(177.70);
        assertEquals(result, new Montant(6.99));
    }

    @Test
    public void isValid_GivenEmpty_ShouldBeFalse() {
        assertFalse(Montant.isValid(""));
    }

    @Test
    public void isValid_Given1_ShouldBeTrue() {
        assertTrue(Montant.isValid("1"));
    }

    @Test
    public void normalize_Given12_225_ShouldBe12_25() {
        assertThat(new Montant(12.225).normalize(), is(new Montant(12.25)));
    }
}
