package ch.globaz.common.domaine;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DateTest {
    private void create100Times(final int nbThreads) throws InterruptedException {
        Callable<Void> task = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (int i = 0; i < 100; i++) {
                    new Date("01.01.2013");
                }
                return null;
            }
        };
        List<Callable<Void>> tasks = Collections.nCopies(nbThreads, task);
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
        executor.invokeAll(tasks);
    }

    @Test
    public void given01012013WithPointsShouldBeValid() {
        new Date("01.01.2013");
    }

    @Test
    public void given01032013WhenGetNumeroMoisThenReturn2() {
        Date date = new Date("01.03.2013");

        assertEquals(3, date.getNumeroMois());
    }

    @Test(timeout = 500)
    @Ignore
    public void given100ThreadsWhichCreate100DatesThenReturnNoException() {
        try {
            create100Times(5);
        } catch (InterruptedException e) {
            fail("Problème de multithreading");
        }
    }

    @Test
    public void given20130101And20130101WhenEqualsShouldBeTrue() {
        Date date1 = new Date("20130101");
        Date date2 = new Date("20130101");
        assertTrue(date1.equals(date2));
    }

    @Test
    public void given20130101And5WhenEqualsShouldBeFalse() {
        Date date1 = new Date("20130101");
        int two = 2;
        assertFalse(date1.equals(two));
    }

    @Test
    public void given20130101ShouldBeValid() {
        new Date("20130101");
    }

    @Test
    public void given20130101WhenGetValueShouldBe20130101() {
        String actual = new Date("20130101").getValue();
        assertEquals("20130101", actual);
    }

    @Test
    public void given20131301ShouldBeInvalid() {
        try {
            new Date("20131301");
            fail("La date 20131301 n'est pas une date valide et l'exception n'a pas été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void givenEmptyShouldBeInvalid() {
        boolean actual = Date.isValid("");
        assertEquals(actual, false);
    }

    @Test
    public void givenEmptyShouldBeValid() {
        try {
            new Date("");
            fail("La date 'Empty' n'est pas censé être valide. Cependant, aucune exception n'a été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void givenMarsDateWhenIsSemestrielThenReturnTrue() {
        Date date = new Date("03.2014");

        assertTrue(date.isMoisTrimestriel());
    }

    @Test
    public void givenMonthDateShouldBeValidShouldBeValid() {
        new Date("04.2014");
    }

    @Test
    public void givenA042014DateWhenAddMonthShouldReturnADateOf052014() {
        Date dateWithOneMoreMonth = new Date("04.2014").addMonth(1);

        assertEquals("01.05.2014", dateWithOneMoreMonth.getSwissValue());
    }

    @Test
    public void givenA122014DateWhenIsAnnuelThenReturnTrue() {
        Date date = new Date("12.2014");

        assertTrue(date.isMoisAnnuel());
    }

    @Test
    public void givenA102014DateWhenIsAnnuelThenReturnTrue() {
        Date date = new Date("10.2014");

        assertFalse(date.isMoisAnnuel());
    }

    @Test
    public void givenA102014DateWhenGetFirstDayOfYearThenReturnANewDateWithTheFirstDayOfTheYear() {
        Date date = new Date("10.2014");

        Date expected = new Date("01.01.2014");

        assertEquals(expected, date.getDateOfFirstDayOfYear());
    }

    @Test
    public void givenA102014DateWhenAddYearThenReturnANewDateWithOneMoreYear() {
        Date date = new Date("10.2014");
        Date dateWithOneMoreYear = date.addYear(1);

        Date expected = new Date("10.2015");

        assertEquals(expected, dateWithOneMoreYear);
    }

    @Test
    public void getCurrentPeriodeTrimestrielle_given01122015_ShouldBe1() {
        Date date = new Date("01.12.2015");

        assertEquals(4, date.getCurrentPeriodeTrimestrielle());
    }

    @Test
    public void getCurrentPeriodeTrimestrielle_given01032015_ShouldBe2() {
        Date date = new Date("01.03.2015");

        assertEquals(1, date.getCurrentPeriodeTrimestrielle());
    }

    @Test
    public void getCurrentPeriodeTrimestrielle_given01062015_ShouldBe3() {
        Date date = new Date("01.06.2015");

        assertEquals(2, date.getCurrentPeriodeTrimestrielle());
    }

    @Test
    public void getCurrentPeriodeTrimestrielle_given01092015_ShouldBe4() {
        Date date = new Date("01.09.2015");

        assertEquals(3, date.getCurrentPeriodeTrimestrielle());
    }

    @Test
    public void addDay10_given10052013_ShouldReturn20052013() {
        Date date = new Date("10.05.2013");
        Date datePlus10Days = date.addDays(10);
        assertEquals(new Date("20.05.2013"), datePlus10Days);
    }

    @Test
    public void beforeOrEquals_givenTwoDifferentsDatesWhereDate1IsBefore_ShouldReturnTrue() {
        Date date1 = new Date("10.05.2013");
        Date date2 = new Date("11.05.2013");

        assertTrue(date1.beforeOrEquals(date2));
    }

    @Test
    public void beforeOrEquals_givenTwoSameDates_ShouldReturnTrue() {
        Date date1 = new Date("10.05.2013");
        Date date2 = new Date("10.05.2013");

        assertTrue(date1.beforeOrEquals(date2));
    }

    @Test
    public void beforeOrEquals_givenTwoDifferentsDatesWhereDate1IsAfter_ShouldBeFalse() {
        Date date1 = new Date("11.05.2013");
        Date date2 = new Date("10.05.2013");

        assertFalse(date1.beforeOrEquals(date2));
    }

    @Test
    public void isMonthDate_Given2013011_ShouldBeFalse() {
        assertFalse(Date.isMonthDate("2013011"));
    }

    @Test
    public void isMonthDate_Given01_20131_ShouldBeFalse() {
        assertFalse(Date.isSwissMonthDate("01.20131"));
    }

    @Test
    public void isPeriode_Given01_2013_ShouldReturnTrue() {
        assertTrue(Date.isPeriode("01.2013"));
    }

    @Test
    public void isPeriode_Given201301_ShouldReturnTrue() {
        assertTrue(Date.isPeriode("201301"));
    }

    @Test
    public void isPeriode_Given2013011_ShouldReturnFalse() {
        assertFalse(Date.isPeriode("2013011"));
    }

    @Test
    public void lastDayOfMonth_Given201301_ShouldReturn31_01_01() {
        Date date = Date.lastDayOfMonth("201301");

        assertEquals(new Date("31.01.2013"), date);
    }

    @Test
    public void getMois_Given201304AndLocaleFR_ShouldReturnAvril() {
        Date date = new Date("201304");
        String actual = date.getMois(Locale.FRANCE);
        assertEquals("avril", actual);
    }

    @Test
    public void givenJavaDateWithSecondWhenGetTime() {
        final Calendar calendarExpected = Calendar.getInstance();
        calendarExpected.set(Calendar.YEAR, 2014);
        calendarExpected.set(Calendar.MONTH, 3);
        calendarExpected.set(Calendar.DAY_OF_MONTH, 25);
        calendarExpected.set(Calendar.HOUR_OF_DAY, 0);
        calendarExpected.set(Calendar.MINUTE, 0);
        calendarExpected.set(Calendar.SECOND, 0);
        calendarExpected.set(Calendar.MILLISECOND, 0);

        // long timespan = 1398376800000L;
        // long timespan = calendarExpected.getTimeInMillis();

        long timespan = calendarExpected.getTime().getTime();

        // Conversion d'un objet domaine Date en Calendar et ajout de temps
        // (millisecondes, minutes, secondes, heures)
        final Date date = new Date("25.04.2014");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.getDate());
        calendar.add(Calendar.HOUR, 10);
        calendar.add(Calendar.MINUTE, 7);
        calendar.add(Calendar.SECOND, 22);
        calendar.add(Calendar.MILLISECOND, 10);

        // Récupération de l'objet date généré et création des deux types
        // d'objet.
        final java.util.Date javaDate = calendar.getTime();
        final Date realDate = new Date(calendar.getTime());

        // vérification que la javaDate ne correspond pas au timepsan espéré
        // (soit 25.04.2014 00:00:00)
        assertFalse(javaDate.getTime() == timespan);
        assertTrue(realDate.getTime() == timespan);
    }

    @Test
    public void given4WhenGetMonthNameThenReturnAvril() {
        String expected = "avril";

        assertEquals(expected, Date.getMonthName(4, Locale.FRANCE));
    }

    @Test
    public void given1WhenGetMonthNameThenReturnJanvier() {
        String expected = "janvier";

        assertEquals(expected, Date.getMonthName(1, Locale.FRANCE));
    }

    @Test
    public void given12WhenGetMonthNameThenReturnDecembre() {
        String expected = "décembre";
        assertEquals(expected, Date.getMonthName(12, Locale.FRANCE));
    }

    @Test
    public void given13WhenGetWrittenMonthThenThrowIllegalArgumentException() {
        try {
            Date.getMonthName(13, Locale.FRANCE);
            fail("13 n'est pas un paramètre valide et l'exception n'a pas été levée");
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void given0WhenGetMonthNameThenThrowIllegalArgumentException() {
        try {
            Date.getMonthName(0, Locale.FRANCE);
            fail("0 n'est pas un paramètre valide et l'exception n'a pas été levée");
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void getLastDayOfMonth_Given062014_ShouldBe30062014() {
        Date date = new Date("06.2014");
        assertEquals(new Date("30.06.2014"), date.getLastDayOfMonth());
    }

    @Test
    public void getFirstDayOfYear_Given2014_ShouldBe01_01_2014() {
        Date date = Date.getFirstDayOfYear(2014);

        assertEquals(new Date("01.01.2014"), date);
    }

    @Test
    public void getLastDayOfYear_Given2014_ShouldBe31_12_2014() {
        Date date = Date.getLastDayOfYear(2014);

        assertEquals(new Date("31.12.2014"), date);
    }

    @Test
    public void getMois_Given01122014_ShouldBe12() {
        Date date = new Date("01.12.2014");
        assertEquals(date.getMois(), "12");
    }

    @Test
    public void afterOrEquals01012012_Given01012012_ShouldBeTrue() {
        Date date = new Date("01.12.2014");
        assertTrue(date.afterOrEquals(date));
    }

    @Test
    public void afterOrEquals01012015_Given01012014_ShouldBeTrue() {
        Date date = new Date("01.12.2014");
        Date dateAfter = new Date("01.01.2015");
        assertTrue(dateAfter.afterOrEquals(date));
    }

    @Test
    public void getMois12_Given01012014_ShouldBeDecember() {
        Date date = new Date("01.01.2014");
        Date dateDecembre = date.getMois(12);
        assertEquals(dateDecembre.getMois(Locale.FRANCE), "décembre");
    }

    @Test
    public void getMois02_Given01012014_ShouldBeDecember() {
        Date date = new Date("01.01.2014");
        Date dateDecembre = date.getMois(2);
        assertEquals(dateDecembre.getMois(Locale.FRANCE), "février");
    }

    @Test
    public void getMoisAnneeFormatte_Given012014_ShouldBe01_2014() {
        Date date = new Date("01.2014");
        assertEquals("01.2014", date.getMoisAnneeFormatte());
    }

    @Test
    public void getAnnee_Given0120_ShouldBe0020() {
        Date date = new Date("01.20");
        assertEquals("0020", date.getAnnee());
    }

    @Test
    public void isValid_Given1_01_20_ShouldBeFalse() {
        assertFalse(Date.isValid("1.01.20"));
    }

    @Test
    public void testGetYear() throws Exception {
        Date date = new Date("01.2014");
        assertEquals(2014, date.getYear());
    }

    @Test
    public void testGetSwissMonthValue() throws Exception {
        Date date = new Date("01.03.2014");
        assertEquals("03.2014", date.getSwissMonthValue());
    }

    @Test
    public void testEqualsByMonthYearTrue() throws Exception {
        Date date = new Date("01.03.2014");
        assertTrue(date.equalsByMonthYear(new Date("05.03.2014")));
        assertTrue(date.equalsByMonthYear(new Date("01.03.2014")));
    }

    @Test
    public void testEqualsByMonthYeareFalse() throws Exception {
        Date date = new Date("01.03.2014");
        assertFalse(date.equalsByMonthYear(new Date("05.06.2014")));
        assertFalse(date.equalsByMonthYear(new Date("01.06.2014")));
    }

    @Test
    public void testGetFirstMonday() throws Exception {
        Date date = new Date("03.07.2014");
        Date expected = new Date("07.07.2014");
        assertEquals(expected, date.getFirstMonday());

        date = new Date("17.07.2014");
        assertEquals(expected, date.getFirstMonday());
    }

    @Test
    public void testIsSundayTrue() throws Exception {
        assertTrue(new Date("05.07.2015").isSunday());
    }

    @Test
    public void testIsSundayFalse() throws Exception {
        assertFalse(new Date("06.07.2015").isSunday());
    }

    @Test
    public void testGetValueMonth() throws Exception {
        assertEquals("201507", new Date("05.07.2015").getValueMonth());
    }

    @Test
    public void testToMonthYear() throws Exception {
        assertEquals(new Date("07.2015"), new Date("05.07.2015").toMonthYear());
        assertEquals(new Date("07.2015"), new Date("07.2015").toMonthYear());
    }

    @Test
    public void testGetNbDaysInYear() throws Exception {
        Date date = new Date("10.10.2016");
        assertEquals(366, date.getNbDaysInYear());
        date = new Date("10.10.2015");
        assertEquals(365, date.getNbDaysInYear());
    }

    @Test
    public void testForPartialDate0000() throws Exception {
        Date date = Date.forPartialDate("20160000");
        assertEquals(new Date("01.01.2016"), date);
    }

    @Test
    public void testForPartialDate00() throws Exception {
        Date date = Date.forPartialDate("20160400");
        assertEquals(new Date("01.04.2016"), date);
    }

    @Test
    public void testForPartialDateGeneric() throws Exception {
        Date date = Date.forPartialDate("20160410");
        assertEquals(new Date("10.04.2016"), date);
    }

}
