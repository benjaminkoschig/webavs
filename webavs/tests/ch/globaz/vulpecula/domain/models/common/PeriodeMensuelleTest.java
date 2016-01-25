package ch.globaz.vulpecula.domain.models.common;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Locale;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class PeriodeMensuelleTest {
    @Test
    public void givenAValidPeriodeWhentoStringShouldReturn032014To052014() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("201403", "201405");

        Assert.assertEquals("03.2014 - 05.2014", periodeMensuelle.toString());
    }

    @Test
    public void givenAValidPeriodInSwissValueShouldBeValid() {
        new PeriodeMensuelle("03.2014", "03.2014");
    }

    @Test
    public void givenAValidStrangePeriodInSwissValueShouldBeValid() {
        new PeriodeMensuelle("014.03.2", "014.03.2");
    }

    @Test
    public void givenAValidPeriodInSwissValueWhenGetPropertiesShouldBeCorrect() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("03.2014", "05.2014");

        Assert.assertEquals("03.2014 - 05.2014", periodeMensuelle.toString());
    }

    @Test
    public void given032014052014WhenPeriodeDebutAsValueShouldReturn032014() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("03.2014", "05.2014");

        Assert.assertEquals(periodeMensuelle.getPeriodeDebutAsValue(), "201403");
    }

    @Test
    public void given032014And052015WhenPeriodeFinAsValueShouldReturn052014() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("03.2014", "05.2014");

        Assert.assertEquals(periodeMensuelle.getPeriodeFinAsValue(), "201405");
    }

    @Test
    public void given032014And052015WhenGetMonthsDateThenReturn3Dates() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("03.2014", "05.2014");

        Assert.assertThat(periodeMensuelle.getMois(),
                Matchers.hasItems(new Date("03.2014"), new Date("04.2014"), new Date("05.2014")));
        Assert.assertThat(periodeMensuelle.getMois(),
                Matchers.not(Matchers.hasItems(new Date("02.2014"), new Date("06.2014"))));
    }

    @Test
    public void given112015And012016WhenGetMonthsDateThenReturn3Dates() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("11.2015", "01.2016");

        Assert.assertThat(periodeMensuelle.getMois(),
                Matchers.hasItems(new Date("11.2015"), new Date("12.2015"), new Date("01.2016")));
        Assert.assertThat(periodeMensuelle.getMois(),
                Matchers.not(Matchers.hasItems(new Date("10.2015"), new Date("02.2015"))));
    }

    @Test
    public void given012015And032015WhenGetNombreMoisThenReturn2() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "03.2015");

        Assert.assertEquals(2, periodeMensuelle.getNombreMois());
    }

    @Test
    public void given012015And062015WhenGetNombreMoisThenReturn5() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "06.2015");

        Assert.assertEquals(5, periodeMensuelle.getNombreMois());
    }

    @Test
    public void given012015And032015WhenIsTrimestrielThenReturnTrue() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "03.2015");

        Assert.assertTrue(periodeMensuelle.isTrimestriel());
    }

    @Test
    public void given012015And042015WhenIsTrimestrielMoisThenReturnTrue() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "04.2015");

        Assert.assertFalse(periodeMensuelle.isTrimestriel());
    }

    @Test
    public void given012015And012015WhenIsMensuelleThenReturnTrue() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "01.2015");

        Assert.assertTrue(periodeMensuelle.isMensuelle());
    }

    @Test
    public void given012015And022015WhenIsMensuelleThenReturnFalse() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "02.2015");

        Assert.assertFalse(periodeMensuelle.isMensuelle());
    }

    @Test
    public void given012015And012015WhenGetmoisThenReturnAListOfOneMonth() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "01.2015");

        Date[] dates = { new Date("01.2015") };

        Assert.assertThat(periodeMensuelle.getMois(), Matchers.equalTo(Arrays.asList(dates)));
    }

    @Test
    public void a012015PeriodWhenGetMoisAnneeAsSwissValueThenReturn012015() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "01.2017");

        String actual = periodeMensuelle.getPeriodeDebutAsSwissValue();

        Assert.assertEquals("01.2015", actual);
    }

    @Test
    public void a012015PeriodWhenGetMoisAnneeAsSwissValueThenReturn012017() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2015", "01.2017");

        String actual = periodeMensuelle.getPeriodeFinAsSwissValue();

        Assert.assertEquals("01.2017", actual);
    }

    @Test
    public void a052015PeriodWhenGetFirstDayOfYearThenReturnADateOf01012015() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("05.2015", "07.2015");

        Date actual = periodeMensuelle.getFirstOfYearOfDateDebut();
        Date expected = new Date("01.01.2015");

        assertEquals(expected, actual);
    }

    @Test
    public void a052015_082015PeriodWhenIsLongerThanOneMonthThenReturnTrue() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("05.2015", "08.2015");

        assertTrue(periodeMensuelle.isLongerThanOneMonth());
    }

    @Test
    public void a052015_052015PeriodWhenIsLongerThanOneMonthThenReturnFalse() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("05.2015", "05.2015");

        assertFalse(periodeMensuelle.isLongerThanOneMonth());
    }

    @Test
    public void getPeriodeDebutMoisAnnee_GivenPeriodeof042015And052015_ShouldBeAvril2015() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("04.2015", "04.2015");
        String actual = periodeMensuelle.getPeriodeDebutMoisAnnee(Locale.FRANCE);

        assertEquals("avril 2015", actual);
    }

    @Test
    public void getDateDebutAsSwissValue_GivenPeriodeOf042014And052015_ShouldBe01_04_2015() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("04.2014", "05.2014");
        assertEquals("01.04.2014", periodeMensuelle.getPeriodeDebutWithDay());
    }

    @Test
    public void getDateDebutAsSwissValue_GivenPeriodeOf042014And052015_ShouldBe31_05_2015() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("04.2014", "05.2014");
        assertEquals("31.05.2014", periodeMensuelle.getPeriodeFinWithDay());
    }

}
