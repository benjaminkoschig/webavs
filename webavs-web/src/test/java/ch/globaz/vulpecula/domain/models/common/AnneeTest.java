package ch.globaz.vulpecula.domain.models.common;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.List;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AnneeTest {

    @Test(expected = IllegalArgumentException.class)
    @Parameters
    public void givenInvalidYearExceptionShouldBeThrown(int invalidYear) {
        new Annee(invalidYear);
    }

    @Test
    @Parameters
    public void givenValidYearShouldBeValid(int validYear) {
        new Annee(validYear);
    }

    @Test
    public void given0WhenIsAfter2000ShouldBeFalse() {
        Annee zero = new Annee(0);
        Annee twoThousand = new Annee(2000);
        assertThat(zero.isAfter(twoThousand), is(false));
    }

    @Test
    public void given1999WhenIsAfter0ShouldBeTrue() {
        Annee nineteenNinetyNine = new Annee(1999);
        Annee zero = new Annee(0);
        assertThat(nineteenNinetyNine.isAfter(zero), is(true));
    }

    @Test
    public void given1950WhenCompareTo1950ShouldBe0() {
        Annee nineteenFifty = new Annee(1950);
        assertThat(nineteenFifty.compareTo(nineteenFifty), is(0));
    }

    @Test
    public void given1950WhenCompareTo1980ShouldBeNegative() {
        Annee nineteenFifty = new Annee(1950);
        Annee nineteenEighty = new Annee(1980);
        assertThat(nineteenFifty.compareTo(nineteenEighty), is(lessThanOrEqualTo(-1)));
    }

    @Test
    public void given2010WhenCompareTo1980ShouldBePositive() {
        Annee twoThousandTen = new Annee(2010);
        Annee nineteenEighty = new Annee(1980);
        assertThat(twoThousandTen.compareTo(nineteenEighty), is(greaterThanOrEqualTo(1)));
    }

    @Test
    public void given2010AsStringShouldBeOk() {
        new Annee("2010");
    }

    @Test(expected = NumberFormatException.class)
    public void givenASDAsStringShouldBeKo() {
        new Annee("ASD");
    }

    @Test
    public void getFirstDayOfYear_OfYear2014_ShouldBe01_01_2014() {
        Date date = new Annee(2014).getFirstDayOfYear();

        assertEquals(date, new Date("01.01.2014"));
    }

    @Test
    public void getLastDayOfYear_OfYear2014_ShouldBe31_12_2014() {
        Date date = new Annee(2014).getLastDayOfYear();

        assertEquals(date, new Date("31.12.2014"));
    }

    @Test
    public void between_WithYearOf2010And2014_ShouldReturn2010_2011_2012_2013_2014() {
        List<Annee> annees = Annee.between(2010, 2014);

        assertThat(annees,
                contains(new Annee(2010), new Annee(2011), new Annee(2012), new Annee(2013), new Annee(2014)));
    }

    @Test
    public void isContains2012_Given2014_2016_ShouldBeFalse() {
        Annee annee = new Annee(2012);
        boolean value = annee.isContained(new Annee(2014), new Annee(2016));

        assertThat(value, is(false));
    }

    @Test
    public void isContains2014_Given2014_2016_ShouldBeFalse() {
        Annee annee = new Annee(2014);
        boolean value = annee.isContained(new Annee(2014), new Annee(2016));

        assertThat(value, is(true));
    }

    @Test
    public void isContains2015_Given2014_2016_ShouldBeFalse() {
        Annee annee = new Annee(2015);
        boolean value = annee.isContained(new Annee(2014), new Annee(2016));

        assertThat(value, is(true));
    }

    @Test
    public void isContains2016_Given2014_2016_ShouldBeFalse() {
        Annee annee = new Annee(2016);
        boolean value = annee.isContained(new Annee(2014), new Annee(2016));

        assertThat(value, is(true));
    }

    @Test
    public void isContains2017_Given2014_2016_ShouldBeFalse() {
        Annee annee = new Annee(2017);
        boolean value = annee.isContained(new Annee(2014), new Annee(2016));

        assertThat(value, is(false));
    }

    @Test
    public void next_WithYearOf2010ShouldBe2011() {
        Annee nextYear = new Annee(2010).next();

        assertThat(nextYear, is(new Annee(2011)));
    }

    @Test
    public void isAfterOrEquals_Given2010_ShouldBeValidWith2010() {
        Annee annee = new Annee(2010);
        assertThat(annee.isAfterOrEquals(annee), is(true));
    }

    @Test
    public void isBeforeOrEquals_Given2010_ShouldBeValidWith2010() {
        Annee annee = new Annee(2010);
        assertThat(annee.isBeforeOrEquals(annee), is(true));
    }

    @Test
    public void isBefore_Given2010_ShouldBeInvalidWith2010() {
        Annee annee = new Annee(2010);
        assertThat(annee.isBefore(annee), is(false));
    }

    @Test
    public void isBefore_Given2010_ShouldBeValidWith2009() {
        Annee annee = new Annee(2009);
        Annee other = new Annee(2010);
        assertThat(annee.isBefore(other), is(true));
    }

    @Test
    public void previous5_Given2010_ShouldBe2005() {
        assertThat(new Annee(2010).previous(5), is(new Annee(2005)));
    }

    @SuppressWarnings("unused")
    private Object[] parametersForGivenValidYearShouldBeValid() {
        return new Object[][] { { 0 }, { 1 }, { 1500 }, { 1900 }, { 1980 }, { 1999 }, { 2000 }, { 2019 }, { 2099 },
                { 9999 } };
    }

    @SuppressWarnings("unused")
    private Object[] parametersForGivenInvalidYearExceptionShouldBeThrown() {
        return new Object[][] { { -1 }, { -123 }, { -1999 }, { 10000 }, { 100000 }, { 99999 } };
    }

}
