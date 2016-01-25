package ch.globaz.utils.periode;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class DateResolverTestCase {

    private void generateListDate(List<String> dates) {
        dates.add("01.2012");
        dates.add("05.2012");
        dates.add("12.2012");
        dates.add("01.2013");
        dates.add("11.2013");
        dates.add("");
        dates.add("01.02.2010");
    }

    @Test
    public void testResolveDateMonthYeartMax() {
        List<String> dates = new ArrayList<String>();
        generateListDate(dates);
        Assert.assertEquals("11.2013", GroupePeriodesResolver.resolveDateMax(dates));
    }

    @Test
    public void testResolveDateMonthYeartMin() {
        List<String> dates = new ArrayList<String>();
        generateListDate(dates);
        Assert.assertEquals("01.2012", GroupePeriodesResolver.resolveDateMin(dates));
    }

    @Test
    public void testResolveDateMonthYeartMinReturnNullValue() {
        List<String> dates = new ArrayList<String>();
        generateListDate(dates);
        Assert.assertNull(GroupePeriodesResolver.resolveDateMin(dates, true));
    }
}
