package globaz.prestation.pojo;

import globaz.prestation.beans.PRPeriode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class PRPeriodeTest {

    @Test
    public void testComparable() {
        PRPeriode p1 = new PRPeriode("01.02.2013", "05.02.2013");
        PRPeriode p2 = new PRPeriode("01.01.2013", "04.01.2013");
        PRPeriode p3 = new PRPeriode("01.01.2012", "05.01.2012");
        PRPeriode p4 = new PRPeriode("01.01.2013", "12.01.2014");
        List<PRPeriode> list = new ArrayList<PRPeriode>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        printPeriodeList(list);
        System.out.println("----------------------");
        Collections.sort(list);
        Assert.assertTrue(list.get(0).getDateDeDebut().equals("01.01.2012"));
        Assert.assertTrue(list.get(1).getDateDeDebut().equals("01.01.2013"));
        Assert.assertTrue(list.get(2).getDateDeDebut().equals("01.01.2013"));
        Assert.assertTrue(list.get(3).getDateDeDebut().equals("01.02.2013"));
        printPeriodeList(list);
    }

    private void printPeriodeList(List<PRPeriode> periodes) {
        for (PRPeriode periode : periodes) {
            System.out.println(periode.getDateDeDebut() + " - " + periode.getDateDeFin());
        }
    }
}
