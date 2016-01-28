package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc.ListSortedByDate.DatesStringToCompare;

public class ListSortedByDateTest {

    private List<String> createList() {
        List<String> list = new ArrayList<String>();
        list.add("01.2015");
        list.add("01.2014");
        list.add("06.2014");
        list.add("06.2015");
        list.add("03.2015");
        list.add("02.2014");
        return list;
    }

    @Test
    public void testSortDateMmYYYYAsc() throws Exception {
        List<String> list = createList();
        ListSortedByDate.sortDateMmsyyyyAsc(list, new DatesStringToCompare<String>() {
            @Override
            public String[] getDates(String val1, String val2) {
                return new String[] { val1, val2 };
            }
        });

        Assert.assertEquals("01.2014", list.get(0));
        Assert.assertEquals("06.2015", list.get(5));
    }

    @Test
    public void testSortDateMmYYYYDesc() throws Exception {
        List<String> list = createList();
        ListSortedByDate.sortDateMmsyyyyDesc(list, new DatesStringToCompare<String>() {
            @Override
            public String[] getDates(String val1, String val2) {
                return new String[] { val1, val2 };
            }
        });

        Assert.assertEquals("06.2015", list.get(0));
        Assert.assertEquals("01.2014", list.get(5));
    }

}
