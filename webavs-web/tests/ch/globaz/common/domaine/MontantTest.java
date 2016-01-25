package ch.globaz.common.domaine;

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
    public void testNewMontantAnnualiseString() throws Exception {
        Montant montant = Montant.newAnnuel("10");
        assertEquals(new BigDecimal(10), montant.getCurrency());
        assertTrue(montant.isAnnuel());
    }

    @Test
    public void testAnnualiseMensuel() throws Exception {
        Montant montant = Montant.newMensuel("10");
        Montant montantAnualise = montant.annualise();
        assertEquals(new BigDecimal(120), montantAnualise.getCurrency());
        assertTrue(montantAnualise.isAnnuel());
    }

    @Test
    public void testAnnualiseJournalier() throws Exception {
        Montant montant = Montant.newJouranlier("10");
        Montant montantAnualise = montant.annualise();

        assertEquals(Montant.newAnnuel(2604), montantAnualise);
        assertTrue(montantAnualise.isAnnuel());
    }

    @Test
    public void testNewMontantMensuelString() throws Exception {
        String m = null;
        Montant montant = Montant.newMensuel("10");
        assertEquals(new BigDecimal(10), montant.getCurrency());
        assertTrue(montant.isMensuel());
    }

    @Test
    public void testAddAnnuelPeriodicity() throws Exception {
        Montant montant = new Montant("100");
        Montant montant1 = montant.addAnnuelPeriodicity();
        assertTrue(montant.isSansPeriode());
        assertTrue(montant1.isAnnuel());
    }

    @Test
    public void testAddMensuelPeriodicity() throws Exception {
        Montant montant = new Montant("100");
        Montant montant1 = montant.addMensuelPeriodicity();
        assertTrue(montant.isSansPeriode());
        assertTrue(montant1.isMensuel());
    }

    @Test
    public void testAddJournalierPeriodicity() throws Exception {
        Montant montant = new Montant("100");
        Montant montant1 = montant.addJournalierPeriodicity();
        assertTrue(montant.isSansPeriode());
        assertTrue(montant1.isJouranlier());
    }

    // @Test(expected = IllegalStateException.class)
    // public void testCheckPeriodicityMutable() throws Exception {
    // Montant montant = Montant.newJouranlier("10");
    // montant.addMensuelPeriodicity();
    // }

    @Test
    public void testEquals() throws Exception {
        Montant montant = new Montant("100");
        montant.addJournalierPeriodicity();
        assertEquals(new Montant("100"), new Montant("100"));
    }

    @Test
    public void testGetValueDouble() throws Exception {
        Montant montant = new Montant("100.5454");
        assertEquals(new Double(100.55), montant.getValueDouble());
    }

    @Test
    public void testArrondiAUnIntier100() throws Exception {
        Montant montant = new Montant("100");
        assertEquals(new Montant(100), montant.arrondiAUnIntier());
    }

    @Test
    public void testArrondiAUnIntier100_25() throws Exception {
        Montant montant = new Montant("100.25");
        assertEquals(new Montant(100), montant.arrondiAUnIntier());
    }

    @Test
    public void testArrondiAUnIntier100_50() throws Exception {
        Montant montant = new Montant("100.50");
        assertEquals(new Montant(101), montant.arrondiAUnIntier());
    }

    @Test
    public void testArrondiAUnIntier100_75() throws Exception {
        Montant montant = new Montant("100.75");
        assertEquals(new Montant(101), montant.arrondiAUnIntier());
    }

    @Test
    public void testToStringFormat() throws Exception {
        Montant montant = new Montant("100");
        assertEquals("100.00", montant.toStringFormat());
    }

    @Test
    public void testToStringFormat1() throws Exception {
        Montant montant = new Montant("0");
        assertEquals("0.00", montant.toStringFormat());
    }

}
