package ch.globaz.common.domaine;

import org.junit.Assert;
import org.junit.Test;

public class GroupePeriodesTest {

    private static final String dateDebutMax = "06.2013";
    private static final String dateDebutMin = "01.2000";
    private static final String dateFinMax = "10.2013";
    private static final String dateFinMin = "08.2000";

    private GroupePeriodes generateDates() {
        GroupePeriodes datesDebutFin = new GroupePeriodes();
        datesDebutFin.add("01.2000", "08.2000");
        datesDebutFin.add("01.2013", "05.2013");
        datesDebutFin.add("06.2013", "10.2013");
        datesDebutFin.add("08.2012", "12.2012");
        return datesDebutFin;
    }

    @Test
    public void testAdd() {
        GroupePeriodes periodes = generateDates();
        Assert.assertEquals(4, periodes.getPeriodes().size());
    }

    @Test
    public void testDateDebutHasNullVallueFalse() {
        GroupePeriodes datesDebutFin = generateDates();
        Assert.assertFalse(datesDebutFin.hasDateDebutNullValue());
    }

    @Test
    public void testDateDebutHasNullVallueTrue() {
        GroupePeriodes periodes = generateDates();
        periodes.add("", "07.2012");
        Assert.assertEquals(GroupePeriodesTest.dateDebutMax, periodes.getDateDebutMax());
        Assert.assertEquals(GroupePeriodesTest.dateDebutMin, periodes.getDateDebutMin());
        Assert.assertEquals(GroupePeriodesTest.dateFinMax, periodes.getDateFinMax());
        Assert.assertEquals(GroupePeriodesTest.dateFinMin, periodes.getDateFinMin());
        Assert.assertTrue(periodes.hasDateDebutNullValue());
    }

    @Test
    public void testDateDebutMax() {
        GroupePeriodes periodes = generateDates();
        Assert.assertEquals(GroupePeriodesTest.dateDebutMax, periodes.getDateDebutMax());
    }

    @Test
    public void testDateDebutMin() {
        GroupePeriodes periodes = generateDates();
        Assert.assertEquals(GroupePeriodesTest.dateDebutMin, periodes.getDateDebutMin());
    }

    @Test
    public void testDateDebutNullWithDateFinMax() {
        GroupePeriodes periodes = generateDates();
        periodes.add("", "04.2014");
        Assert.assertEquals(GroupePeriodesTest.dateDebutMax, periodes.getDateDebutMax());
        Assert.assertEquals(GroupePeriodesTest.dateDebutMin, periodes.getDateDebutMin());
        Assert.assertEquals("04.2014", periodes.getDateFinMax());
        Assert.assertEquals(GroupePeriodesTest.dateFinMin, periodes.getDateFinMin());
        Assert.assertTrue(periodes.hasDateDebutNullValue());
    }

    @Test
    public void testDateDebutNullWithDateFinMin() {
        GroupePeriodes periodes = generateDates();
        periodes.add("", "01.1999");
        Assert.assertEquals(GroupePeriodesTest.dateDebutMax, periodes.getDateDebutMax());
        Assert.assertEquals(GroupePeriodesTest.dateDebutMin, periodes.getDateDebutMin());
        Assert.assertEquals(GroupePeriodesTest.dateFinMax, periodes.getDateFinMax());
        Assert.assertEquals("01.1999", periodes.getDateFinMin());
        Assert.assertTrue(periodes.hasDateDebutNullValue());
    }

    @Test
    public void testDateFinHasNullVallueFalse() {
        GroupePeriodes periodes = generateDates();
        Assert.assertFalse(periodes.hasDateFinNullValue());
    }

    @Test
    public void testDateFinHasNullVallueTrue() {
        GroupePeriodes periodes = generateDates();
        periodes.add("07.2012", "");
        Assert.assertEquals(GroupePeriodesTest.dateDebutMax, periodes.getDateDebutMax());
        Assert.assertEquals(GroupePeriodesTest.dateDebutMin, periodes.getDateDebutMin());
        Assert.assertEquals(GroupePeriodesTest.dateFinMax, periodes.getDateFinMax());
        Assert.assertEquals(GroupePeriodesTest.dateFinMin, periodes.getDateFinMin());
        Assert.assertTrue(periodes.hasDateFinNullValue());
    }

    @Test
    public void testDateFinMax() {
        GroupePeriodes periodes = generateDates();
        Assert.assertEquals(GroupePeriodesTest.dateFinMax, periodes.getDateFinMax());
    }

    @Test
    public void testDateFinMin() {
        GroupePeriodes periodes = generateDates();
        Assert.assertEquals(GroupePeriodesTest.dateFinMin, periodes.getDateFinMin());
    }

    @Test
    public void testDateFinNullWithDateDebutMax() {
        GroupePeriodes periodes = generateDates();
        periodes.add("04.2014", "");
        Assert.assertEquals("04.2014", periodes.getDateDebutMax());
        Assert.assertEquals(GroupePeriodesTest.dateDebutMin, periodes.getDateDebutMin());
        Assert.assertEquals(GroupePeriodesTest.dateFinMax, periodes.getDateFinMax());
        Assert.assertEquals(GroupePeriodesTest.dateFinMin, periodes.getDateFinMin());
        Assert.assertTrue(periodes.hasDateFinNullValue());
    }

    @Test
    public void testDateFinNullWithDateDebutMin() {
        GroupePeriodes periodes = generateDates();
        periodes.add("04.1990", "");
        Assert.assertEquals(GroupePeriodesTest.dateDebutMax, periodes.getDateDebutMax());
        Assert.assertEquals("04.1990", periodes.getDateDebutMin());
        Assert.assertEquals(GroupePeriodesTest.dateFinMax, periodes.getDateFinMax());
        Assert.assertEquals(GroupePeriodesTest.dateFinMin, periodes.getDateFinMin());
        Assert.assertTrue(periodes.hasDateFinNullValue());
    }
}